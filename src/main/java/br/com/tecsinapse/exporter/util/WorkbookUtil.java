/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import static br.com.tecsinapse.exporter.util.Constants.LOCAL_DATE_BIGBANG;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import br.com.tecsinapse.exporter.EmptyTableCell;
import br.com.tecsinapse.exporter.ExporterFormatter;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.type.CellType;

public class WorkbookUtil {

    private DataFormat defaultDataFormat;
    private Map<String, DataFormat> dataFormatMap = new HashMap<>();
    private CreationHelper creationHelper;

    public static WorkbookUtil newWorkbookUtil() {
        return new WorkbookUtil();
    }

    public Workbook toWorkBook(Workbook wb, Table table) {
        List<List<TableCell>> matrix = table.getCells();
        List<List<TableCell>> matrixFull = table.toTableCellMatrix();

        creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet();
        int titleRows = 0;
        int r = titleRows;
        int c = 0;
        int maxColumns = -1;
        Map<Integer, Integer> defaultColumnWidth = new HashMap<>();

        defaultDataFormat = wb.createDataFormat();

        ExporterFormatter tableExporterFormatter = table.getExporterFormatter();

        for (List<TableCell> row : matrix) {
            Row sheetRow = sheet.createRow(r);

            for (TableCell tableCell : row) {
                while (matrixFull.get(r - titleRows)
                        .get(c) == EmptyTableCell.EMPTY_CELL) {
                    c++;
                    if (c >= matrixFull.get(r - titleRows).size()) {
                        c = 0;
                        r++;
                    }
                }

                Cell cell = sheetRow.createCell(c);
                if (c > maxColumns) {
                    maxColumns = c;
                }

                if (tableCell.getRowspan() > 1 || tableCell.getColspan() > 1) {
                    int rowStart = r;
                    int rowEnd = rowStart + (tableCell.getRowspan() - 1);
                    int colStart = c;
                    int colEnd = colStart + (tableCell.getColspan() - 1);

                    CellRangeAddress cellRange = new CellRangeAddress(rowStart, rowEnd, colStart, colEnd);
                    sheet.addMergedRegion(cellRange);

                    RegionUtil.setBorderTop(1, cellRange, sheet, wb);
                    RegionUtil.setBorderRight(1, cellRange, sheet, wb);
                    RegionUtil.setBorderBottom(1, cellRange, sheet, wb);
                    RegionUtil.setBorderLeft(1, cellRange, sheet, wb);
                } else if (!table.isAutoSizeColumnSheet()) {
                    Integer maxColumnWidth = defaultColumnWidth.get(c);
                    if (maxColumnWidth == null) {
                        defaultColumnWidth.put(c, tableCell.getDefaultColumnWidth());
                    } else {
                        int defaultWidth = tableCell.getDefaultColumnWidth();
                        if (defaultWidth > maxColumnWidth) {
                            defaultColumnWidth.put(c, defaultWidth);
                        }
                    }
                }

                String format = setConvertedValue(cell, tableCell, tableExporterFormatter);
                setCellStyle(cell, tableCell, wb, format);
                c++;
            }
            r++;
            c = 0;
        }

        if (table.isAutoSizeColumnSheet()) {
            for (int i = 0; i <= maxColumns; ++i) {
                if (sheet instanceof SXSSFSheet) {
                    ((SXSSFSheet)sheet).trackColumnForAutoSizing(i);
                } else {
                    sheet.autoSizeColumn(i, true);
                }
            }
        } else {
            for (int i = 0; i <= maxColumns; ++i) {
                if (defaultColumnWidth.get(i) == null) {
                    if (sheet instanceof SXSSFSheet) {
                        ((SXSSFSheet)sheet).trackColumnForAutoSizing(i);
                    } else {
                        sheet.autoSizeColumn(i, true);
                    }
                } else {
                    sheet.setColumnWidth(i, defaultColumnWidth.get(i));
                }
            }
        }
        return wb;
    }

    private void setCellStyle(Cell cell, TableCell tableCell, Workbook wb, String cellFormat) {
        if (tableCell.getCellType() == CellType.BRL_TYPE) {
            cellFormat = "_$R$ #,##0.00";
        }
        cell.setCellStyle(tableCell.getTableCellStyle().toCellStyle(wb, cellFormat));
    }

    private String setConvertedValue(Cell cell, TableCell tableCell, ExporterFormatter tableExporterFormatter) {
        Object cellValue = tableCell.getContentObject();
        if (cellValue == null) {
            return null;
        }
        if (tableCell.getCellType().isAllowFormat()) {
            ExporterFormatter cellExporterFormatter = tableCell.getExporterFormatter();
            ExporterFormatter exporterFormatter = cellExporterFormatter == null ? tableExporterFormatter : tableCell.getExporterFormatter();
            boolean isCurrency = tableCell.getCellType() == CellType.CURRENCY_TYPE;
            String dataFormat = exporterFormatter != null ? exporterFormatter.getStringFormatByType(cellValue, isCurrency) : null;
            if (dataFormat != null && setCellValueByType(cell, cellValue)) {
                return dataFormat;
            }
        }
        cell.setCellValue(tableCell.getFormattedContentInternalFirst(tableExporterFormatter));
        return null;
    }

    private boolean setCellValueByType(Cell cell, Object o) {
        if (o instanceof LocalDateTime) {
            cell.setCellValue(toExcelDate(toDate((LocalDateTime) o)));
            return true;
        }
        if (o instanceof LocalDate) {
            cell.setCellValue(toExcelDate(toDate((LocalDate) o)));
            return true;
        }
        if (o instanceof LocalTime) {
            cell.setCellValue(toExcelDate(toDate((LocalTime) o)));
            return true;
        }
        if (o instanceof Number) {
            Number number = (Number) o;
            cell.setCellValue(number.doubleValue());
            return true;
        }
        return false;
    }

    private double toExcelDate(Date date) {
        return DateUtil.getExcelDate(date);
    }

    private Date toDate(LocalDateTime localDateTime) {
        return localDateTime.toDate();
    }

    private Date toDate(LocalDate localDate) {
        return localDate.toDate();
    }

    private Date toDate(LocalTime localTime) {
        return LOCAL_DATE_BIGBANG.toDateTime(localTime).toDate();
    }

}
