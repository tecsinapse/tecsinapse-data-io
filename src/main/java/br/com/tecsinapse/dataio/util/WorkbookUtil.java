/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import br.com.tecsinapse.dataio.EmptyTableCell;
import br.com.tecsinapse.dataio.ExporterFormatter;
import br.com.tecsinapse.dataio.Table;
import br.com.tecsinapse.dataio.TableCell;
import br.com.tecsinapse.dataio.style.TableCellStyle;
import br.com.tecsinapse.dataio.type.CellType;

public class WorkbookUtil {

    private Map<TableCellStyle, CellStyle> cellStyleMap = new HashMap<>();

    public static WorkbookUtil newWorkbookUtil() {
        return new WorkbookUtil();
    }

    public Workbook toWorkBook(Workbook wb, List<Table> tables) {
        for (Table table : tables) {
            wb = toWorkBook(wb, table);
        }
        return wb;
    }

    public Workbook toWorkBook(Workbook wb, Table table) {
        List<List<TableCell>> matrix = table.getCells();
        List<List<TableCell>> matrixFull = table.toTableCellMatrix();

        replaceColorsPallete(table.getColorsReplaceMap(), wb);

        String sheetName = table.getTitle();
        Sheet sheet = sheetName == null ? wb.createSheet() : wb.createSheet(sheetName);
        int titleRows = 0;
        int r = titleRows;
        int c = 0;
        int maxColumns = -1;
        Map<Integer, Integer> defaultColumnWidth = new HashMap<>();

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
                }
                sheet.autoSizeColumn(i, true);
            }
        } else {
            for (int i = 0; i <= maxColumns; ++i) {
                if (defaultColumnWidth.get(i) == null) {
                    if (sheet instanceof SXSSFSheet) {
                        ((SXSSFSheet)sheet).trackColumnForAutoSizing(i);
                    }
                    sheet.autoSizeColumn(i, true);
                } else {
                    int width = table.getMinOrMaxOrActualCellWidth(defaultColumnWidth.get(i));
                    sheet.setColumnWidth(i, width);
                }
            }
        }
        return wb;
    }

    private void setCellStyle(Cell cell, TableCell tableCell, Workbook wb, String cellFormat) {
        TableCellStyle tableCellStyle = tableCell.getTableCellStyle();
        if (tableCellStyle == null) {
            return;
        }
        tableCellStyle = tableCellStyle.duplicate();
        tableCellStyle.setCellFormat(cellFormat);
        cell.setCellStyle(getOrNewCellStyle(tableCellStyle, wb));
    }

    private CellStyle getOrNewCellStyle(TableCellStyle tableCellStyle, Workbook workbook) {
        CellStyle cellStyle = cellStyleMap.get(tableCellStyle);
        if (cellStyle != null) {
            return cellStyle;
        }
        cellStyle = tableCellStyle.toCellStyle(workbook);
        cellStyleMap.put(tableCellStyle, cellStyle);
        return cellStyle;
    }

    private String setConvertedValue(Cell cell, TableCell tableCell, ExporterFormatter tableExporterFormatter) {
        Object cellValue = tableCell.getContentObject();
        if (cellValue == null) {
            return null;
        }
        if (tableCell.getCellType().isAllowFormat()) {
            String dataFormat = getCellFormat(tableCell, tableExporterFormatter, cellValue);
            if (dataFormat != null && setCellValueByType(cell, cellValue)) {
                return dataFormat;
            }
        }
        cell.setCellValue(tableCell.getFormattedContentInternalFirst(tableExporterFormatter));
        return null;
    }

    private String getCellFormat(TableCell tableCell, ExporterFormatter tableExporterFormatter, Object cellValue) {
        ExporterFormatter cellExporterFormatter = tableCell.getExporterFormatter();
        ExporterFormatter exporterFormatter = cellExporterFormatter == null ? tableExporterFormatter : tableCell.getExporterFormatter();
        if (tableCell.isUserDefinedCellType()) {
            return exporterFormatter.getCellStringFormatByUserType(cellValue, tableCell.getCellType());
        }
        boolean isCurrency = tableCell.getCellType() == CellType.CURRENCY_TYPE;
        return exporterFormatter != null ? exporterFormatter.getCellStringFormatByType(cellValue, isCurrency) : null;
    }

    private boolean setCellValueByType(Cell cell, Object o) {
        if (o instanceof Date) {
            cell.setCellValue(toExcelDate((Date) o));
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

    private void replaceColorsPallete(Map<HSSFColor, HSSFColor> colorsReplaceMap, Workbook wb) {
        if (! (wb instanceof HSSFWorkbook)) {
            return;
        }

        HSSFWorkbook hssfWb = (HSSFWorkbook) wb;
        final HSSFPalette customPalette = hssfWb.getCustomPalette();
        for (Entry<HSSFColor, HSSFColor> e : colorsReplaceMap.entrySet()) {
            short[] rgb = e.getValue().getTriplet();
            customPalette.setColorAtIndex(e.getKey().getIndex(),
                    (byte)rgb[0],
                    (byte)rgb[1],
                    (byte)rgb[2]
            );
        }
    }

    public static byte[] toRgbByte(HSSFColor hssfColor) {
        short[] rgb = hssfColor.getTriplet();
        return new byte[] { (byte)rgb[0], (byte)rgb[1], (byte)rgb[2] };
    }

}
