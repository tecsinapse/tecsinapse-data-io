/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import br.com.tecsinapse.exporter.EmptyTableCell;
import br.com.tecsinapse.exporter.type.CellType;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;

public class WorkbookUtil {

    public static Workbook toWorkBook(Workbook wb, Table table) {
        List<List<TableCell>> matrix = table.getCells();
        List<List<TableCell>> matrixFull = table.toTableCellMatrix();

        Sheet sheet = wb.createSheet();
        int titleRows = 0;
        int r = titleRows;
        int c = 0;
        int maxColumns = -1;
        Map<Integer, Integer> defaultColumnWidth = new HashMap<>();
        CellStyle styleHeader = header(getDefaultCellStyle(wb));
        CellStyle styleBody = body(getDefaultCellStyle(wb));
        CellStyle styleFooter = footer(getDefaultCellStyle(wb));

        final DataFormat dataFormat = wb.createDataFormat();
        CellStyle cellStyle = wb.createCellStyle();

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

                setConvertedValue(cell, tableCell);
                setCellStyle(styleHeader, styleBody, styleFooter, cell, tableCell, wb, dataFormat, cellStyle);
                c++;
            }
            r++;
            c = 0;
        }

        if (table.isAutoSizeColumnSheet()) {
            for (int i = 0; i <= maxColumns; ++i) {
                sheet.autoSizeColumn(i, true);
            }
        } else {
            for (int i = 0; i <= maxColumns; ++i) {
                if (defaultColumnWidth.get(i) == null) {
                    sheet.autoSizeColumn(i, true);
                } else {
                    sheet.setColumnWidth(i, defaultColumnWidth.get(i));
                }
            }
        }
        return wb;
    }

    private static void setCellStyle(CellStyle defaultHeader, CellStyle defaultBody, CellStyle defaultFooter, Cell cell,
                              TableCell tableCell, Workbook wb, DataFormat dataFormat, CellStyle cellStyle) {
        CellStyle style;
        if (tableCell.getSpreadsheetCellStyle() != null) {
            style = getDefaultCellStyle(wb);
            cell.setCellStyle(tableCell.getSpreadsheetCellStyle().toCellStyle(style));
        } else {
            switch (tableCell.getTableCellType()) {
                case HEADER:
                    style = tableCell.isBold() ? header(getDefaultCellStyle(wb)) : defaultHeader;
                    break;
                case BODY:
                    style = tableCell.isBold() ? body(getDefaultCellStyle(wb)) : defaultBody;
                    break;
                case FOOTER:
                    style = tableCell.isBold() ? footer(getDefaultCellStyle(wb)) : defaultFooter;
                    break;
                default:
                    throw new IllegalStateException("CellStyle " + tableCell.getTableCellType() + " is not supported.");
            }
        }

        if (tableCell.isBold()) {
            Font font = wb.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }

        if (tableCell.getCellType() == CellType.BRL_TYPE) {
            cellStyle = getBrlCellStyle(dataFormat, style, cellStyle);
            cell.setCellStyle(cellStyle);
        } else {
            cell.setCellStyle(style);
        }

    }

    private static void setConvertedValue(Cell cell, TableCell tableCell) {
        if (tableCell.getCellType() == CellType.NUMERIC_TYPE || tableCell.getCellType() == CellType.BRL_TYPE) {
            Double dValue = tableCell.getContentAsDoubleOrNull();
            if (dValue != null) {
                cell.setCellValue(dValue.doubleValue());
                return;
            }
        }

        //somente seta valor quando diferente de nulo. Default "" SXSSF não suporta nulos
        final String content = tableCell.getContent();
        if (content != null) {
            cell.setCellValue(content);
        }
    }

    private static CellStyle header(CellStyle style) {
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return style;
    }

    private static CellStyle body(CellStyle style) {
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        return style;
    }

    private static CellStyle footer(CellStyle style) {
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        return style;
    }

    private static CellStyle getBrlCellStyle(DataFormat dataFormat, CellStyle styleFrom, CellStyle style) {
        style.cloneStyleFrom(styleFrom);
        style.setDataFormat(dataFormat.getFormat("_$R$ #,##0.00"));
        return style;
    }

    private static CellStyle getDefaultCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setVerticalAlignment((short) VerticalAlignment.CENTER.ordinal());
        style.setAlignment((short) HorizontalAlignment.CENTER.ordinal());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }

}
