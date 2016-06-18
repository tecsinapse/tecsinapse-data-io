/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Table {

    private String title;
    private boolean autoSizeColumnSheet = true;
    private List<List<TableCell>> cells = new ArrayList<>();

    public void replace(TableCell cell, Integer row, Integer column) {
        cells.get(row).set(column, cell);
	}

	public void replaceLastCell(TableCell cell) {
		replaceLastColumn(getLastRow(), cell);
	}

	public TableCell getLastCell() {
		return getLastRow().get(getLastColumnIndex(getLastRow()));
	}

	public String getLastCellContent() {
		return getLastCell().getContent();
	}

	public void replaceLastCellContent(String content) {
		getLastCell().setContent(content);
	}

	public void replaceLastColumn(List<TableCell> row, TableCell cell) {
		row.set(getLastColumnIndex(row), cell);
	}

	public Integer getLastColumnIndex(List<TableCell> row) {
		return row.size() - 1;
	}

	public Integer getLastRowIndex() {
		return cells.size() - 1;
	}

	public List<TableCell> getLastRow() {
		return cells.get(getLastRowIndex());
	}

	public void addOnRow(TableCell cell, Integer row) {
		cells.get(row).add(cell);
	}

	public void add(TableCell cell) {
		getLastRow().add(cell);
	}

	public void add(String content) {
		add(new TableCell(content));
	}

    public void add(String content, boolean bold) {
        add(new TableCell(content, bold));
    }

    public void add(String content, TableCellType tableCellType) {
        add(new TableCell(content, tableCellType));
    }

    public void add(String content, TableCellType tableCellType, boolean bold) {
        add(new TableCell(content, tableCellType, bold));
    }

    public void add(String content, TableCellType tableCellType, int colspan) {
        add(new TableCell(content, tableCellType, colspan));
    }

    public void add(String content, TableCellType tableCellType, int colspan, int rowspan) {
        add(new TableCell(content, tableCellType, colspan, rowspan));
    }

    public void add(String content, TableCellType tableCellType, String style, int colspan) {
        add(new TableCell(content, tableCellType, style, colspan));
    }

    public void add(String content, TableCellType tableCellType, String style, int colspan, int rowspan) {
        add(new TableCell(content, tableCellType, style, colspan, rowspan));
    }

    public void add(String content, String style) {
        add(new TableCell(content, style));
    }

    public void add(String content, String style, int colspan) {
        add(new TableCell(content, style, colspan));
    }

    public void add(String content, String style, int colspan, int rowspan) {
        add(new TableCell(content, style, colspan, rowspan));
    }

    public void add(String content, int colspan) {
        add(new TableCell(content, colspan));
    }

    public void add(String content, int colspan, int rowspan) {
        add(new TableCell(content, colspan, rowspan));
    }

	public void add(String content, CellType cellType) {
		add(new TableCell(content, cellType));
	}

	public void add(Number content) {
		add(new TableCell(content));
	}

    public void add(Number content, boolean bold) {
        add(new TableCell(content, bold));
    }

	public void addOnNewRow(TableCell cell) {
		addNewRow();
		add(cell);
	}

	public void addNewRow() {
		cells.add(new ArrayList<TableCell>());
	}

    public void addNewRow(List<TableCell> row) {
        cells.add(row);
    }

	public List<List<TableCell>> getCells() {
		return cells;
	}

	public void setCells(List<List<TableCell>> cells) {
		this.cells = cells;
	}

    public void removeFirstRow() {
        if(cells.size() > 0) {
            cells.remove(0);
        }
    }

	public void removeInitialRows(int numberRows) {
		if(cells.size() > numberRows) {
			for(int i = 0; i < numberRows; i++) {
				cells.remove(0);
			}
		}
	}

	public List<List<String>> toStringMatrix() {
		int rows = cells.size();
		int columns = getBiggerRowSize();

		List<List<String>> matrix = new ArrayList<>();

		boolean[][] spanMark = new boolean[rows][];
		for (int r = 0; r < rows; ++r) {
			spanMark[r] = new boolean[columns];
			matrix.add(new ArrayList<String>());
			for (int c = 0; c < columns; ++c) {
				matrix.get(r).add("");
				spanMark[r][c] = false;
			}
		}

		int r = 0;
		int c = 0;
		for (List<TableCell> row : cells) {
			for (TableCell cell : row) {

				// � spanMark e ainda tem colunas
				while (spanMark[r][c] && c < spanMark[r].length - 1) {
					c++;
				}
				if (!spanMark[r][c]) {
					matrix.get(r).set(c, cell.getContent());

					int rowspan = cell.getRowspan();
					int colspan = cell.getColspan();

					if (rowspan == 1 && colspan > 1) {
						for (int i = 1; i < colspan; ++i) {
							spanMark[r][c + i] = true;
						}
					}
					if (colspan == 1 && rowspan > 1) {
						for (int i = 1; i < rowspan; ++i) {
							spanMark[r + i][c] = true;
						}
					}
					if (colspan > 1 && rowspan > 1) {
						for (int i = 1; i < colspan; ++i) {
							for (int j = 1; j < rowspan; ++j) {
								spanMark[r + j][c + i] = true;
							}
						}
					}
				}
				c++;
			}
			c = 0;
			r++;
		}

		return matrix;
	}

	public List<List<TableCell>> toTableCellMatrix() {
		int rows = cells.size();
		int columns = getBiggerRowSize();

		List<List<TableCell>> matrix = new ArrayList<>();

		boolean[][] spanMark = new boolean[rows][];
		for (int r = 0; r < rows; ++r) {
			spanMark[r] = new boolean[columns];
			matrix.add(new ArrayList<TableCell>());
			for (int c = 0; c < columns; ++c) {
				matrix.get(r).add(EmptyTableCell.EMPTY_CELL);
				spanMark[r][c] = false;
			}
		}

		int r = 0;
		int c = 0;
		for (List<TableCell> row : cells) {
			for (TableCell cell : row) {

				// � spanMark e ainda tem colunas
				while (spanMark[r][c] && c < spanMark[r].length - 1) {
					c++;
				}
				if (!spanMark[r][c]) {
					matrix.get(r).set(c, cell);

					int rowspan = cell.getRowspan();
					int colspan = cell.getColspan();

					if (rowspan == 1 && colspan > 1) {
						for (int i = 1; i < colspan; ++i) {
							spanMark[r][c + i] = true;
						}
					}
					if (colspan == 1 && rowspan > 1) {
						for (int i = 1; i < rowspan; ++i) {
							spanMark[r + i][c] = true;
						}
					}
					if (colspan > 1 && rowspan > 1) {
						for (int jr = r; jr < (r + rowspan); jr++) {
							for (int ic = c; ic < (c + colspan); ic++) {
								if (ic != c || jr != r) {
									spanMark[jr][ic] = true;
								}
							}
						}
					}
				}
				c++;
			}
			c = 0;
			r++;
		}

		return matrix;
	}

    private int getBiggerRowSize() {
        int biggerRowSize = 0;
        for (List<TableCell> row : cells) {
            int qtdColumns = 0;
            for(TableCell column : row) {
                qtdColumns += column.getColspan();
                if (qtdColumns > biggerRowSize) {
                    biggerRowSize = qtdColumns;
                }
            }
        }
        return biggerRowSize;
    }

	public void printStringMatrix(List<List<String>> matrix) {
		System.out.println("print matrix");
		for (List<String> row : matrix) {
			for (String cell : row) {
				System.out.print("|" + cell);
			}
			System.out.println();
		}
	}

    public String getStringMatrixAsString(List<List<String>> matrix) {
        StringBuilder sb = new StringBuilder();
        for (List<String> row : matrix) {
            for (String cell : row) {
                sb.append("|");
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

	public void printString() {

		for (List<TableCell> row : cells) {
			for (TableCell cell : row) {
				System.out.print("|" + cell.getContent());
			}
			System.out.println();
		}

	}

	public Workbook toXSSFWorkBook() {
        return toWorkBook(new XSSFWorkbook());
    }

    public Workbook toHSSFWorkBook() {
        return toWorkBook(new HSSFWorkbook());
    }

    /**
     * @return {@link Workbook}
     * @see #toHSSFWorkBook()
     * @deprecated use {@code public Workbook toHSSFWorkBook()} instead.
     */
    @Deprecated
    public Workbook toWorkBook() {
        return toHSSFWorkBook();
    }

	public Workbook toWorkBook(Workbook wb) {
		List<List<TableCell>> matrix = cells;
		List<List<TableCell>> matrixFull = toTableCellMatrix();

		Sheet sheet = wb.createSheet();
		int titleRows = 0;
		int r = titleRows;
		int c = 0;
		int maxColumns = -1;
		Map<Integer,Integer> defaultColumnWidth = new HashMap<>();
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

				// Font font = wb.createFont();
				Cell cell = sheetRow.createCell(c);
				if(c > maxColumns) {
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
            }else if (!isAutoSizeColumnSheet()) {
					Integer maxColumnWidth = defaultColumnWidth.get(c);
					if(maxColumnWidth == null){
						defaultColumnWidth.put(c, tableCell.getDefaultColumnWidth());
					}else{
						int defaultWidth = tableCell.getDefaultColumnWidth();
						if(defaultWidth > maxColumnWidth){
							defaultColumnWidth.put(c, defaultWidth);
						}
					}
				}

				this.setConvertedValue(cell, tableCell);
                this.setCellStyle(styleHeader, styleBody, styleFooter, cell, tableCell, wb, dataFormat, cellStyle);
				c++;
			}
			r++;
			c = 0;
		}

		if(isAutoSizeColumnSheet()){
			for(int i = 0 ; i <= maxColumns ; ++i) {
				sheet.autoSizeColumn(i, true);
			}
		}else{
			for(int i = 0 ; i <= maxColumns ; ++i) {
				if(defaultColumnWidth.get(i) == null){
					sheet.autoSizeColumn(i, true);
				}else{
					sheet.setColumnWidth(i, defaultColumnWidth.get(i));
				}
			}
		}
		return wb;
	}

    private void setCellStyle(CellStyle defaultHeader, CellStyle defaultBody, CellStyle defaultFooter, Cell cell,
							  TableCell tableCell, Workbook wb, DataFormat dataFormat, CellStyle cellStyle){

        CellStyle style;
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

        if(tableCell.isBold()){
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

	private void setConvertedValue(Cell cell, TableCell tableCell){
		if(tableCell.getCellType() == CellType.NUMERIC_TYPE || tableCell.getCellType() == CellType.BRL_TYPE){
			Double dValue = tableCell.getContentAsDoubleOrNull();
			if(dValue != null){
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

	private CellStyle header(CellStyle style) {
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		return style;
	}

	private CellStyle body(CellStyle style) {
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		return style;
	}

	private CellStyle footer(CellStyle style) {
		style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		return style;
	}

	private CellStyle getBrlCellStyle(DataFormat dataFormat, CellStyle styleFrom, CellStyle style) {
		style.cloneStyleFrom(styleFrom);
		style.setDataFormat(dataFormat.getFormat("_$R$ #,##0.00"));
		return style;
	}

	private CellStyle getDefaultCellStyle(Workbook wb) {
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

	@SuppressWarnings("unused")
	private Integer putTitle(Workbook wb, Sheet sheet, int size) {
		Row sheetRow = sheet.createRow(0);

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

		Cell cell = sheetRow.createCell(0);
		sheetRow.setHeight((short) 500);

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, size - 1));

		cell.setCellValue(new HSSFRichTextString(title));

		return 1;
	}

	@SuppressWarnings("unused")
	private void putImage(Workbook wb, Sheet sheet) {
		// add picture data to this workbook.

		int pictureIdx = 0;
		try {
			InputStream is = new FileInputStream(
					"d:/ui-icons_222222_256x240.png");
			byte[] bytes;
			bytes = IOUtils.toByteArray(is);

			pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CreationHelper helper = wb.getCreationHelper();

		// Create the drawing patriarch. This is the top level container for all
		// shapes.
		Drawing drawing = sheet.createDrawingPatriarch();

		// add a picture shape
		ClientAnchor anchor = helper.createClientAnchor();
		// set top-left corner of the picture,
		// subsequent call of Picture#resize() will operate relative to it
		anchor.setCol1(3);
		anchor.setRow1(2);
		Picture pict = drawing.createPicture(anchor, pictureIdx);

		// auto-size picture relative to its top-left corner
		pict.resize();
	}

	public void addAll(List<String> values) {
		if (values != null) {
			for (String value : values) {
				add(value);
			}
		}
	}

	public void addAllCells(List<TableCell> values) {
		if (values != null) {
			for (TableCell cell : values) {
				add(cell);
			}
		}
	}

	public void addAll(List<TableCell> row, List<TableCell> values) {
		row.addAll(values);
	}

	public void concatenateTableOnRight(Table table) {
		// completa se necessario
		int rows = this.getLastRowIndex();
		int rowsOut = table.getLastRowIndex();
		if (rows < rowsOut) {
			int biggerRow = this.getBiggerRowSize();
			List<TableCell> emptyCells = new ArrayList<>();
			for (int i = 0; i < biggerRow; ++i) {
				emptyCells.add(EmptyTableCell.EMPTY_CELL);
			}
			for (int i = 0; i < rowsOut - rows; ++i) {
				this.addNewRow();
				this.addAllCells(emptyCells);
			}
		}

		for (List<TableCell> row : this.cells) {
			row.add(EmptyTableCell.EMPTY_CELL);
		}

		// concatena linhas
		for (int i = 0; i < table.cells.size(); ++i) {
			List<TableCell> row = table.cells.get(i);
			addAll(this.cells.get(i), row);
		}
	}

	public void concatenateTableBelow(Table table) {
		// completa se necessario
		int biggerRow = this.getBiggerRowSize();
		List<TableCell> emptyCells = new ArrayList<>();
		for (int i = 0; i < biggerRow; ++i) {
			emptyCells.add(new TableCell(" "));
		}

		this.addNewRow();
		this.addAllCells(emptyCells);

		// concatena linhas
		for (int i = 0; i < table.cells.size(); ++i) {
			List<TableCell> row = table.cells.get(i);
			this.addNewRow();
			this.addAllCells(row);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public int getNextColumnIndexOfLastRow() {
        return getLastColumnIndex(getLastRow()) + 1;
    }

    /**
     * <p>Este método identifica se ao escrever as colunas da planilha deve ser
     * utilizado o método <code>autoSizeColumn</code> do objeto
     * <code>org.apache.poi.ss.usermodel.Sheet</code>.</p>
     * <p>Se retornar false as colunas serão redimencionadas realizando um
     * cálculo considerando o maior número de caracteres existente em uma coluna</p>
     *
     * @return {@code true} caso esteja configurado {@code auto size}. {@code false} caso contrário
     */
    //#workaround para resolver o problema do tamanho das colunas com valor zero
    public boolean isAutoSizeColumnSheet() {
        return autoSizeColumnSheet;
    }

    public void setAutoSizeColumnSheet(boolean autoSizeColumnSheet) {
        this.autoSizeColumnSheet = autoSizeColumnSheet;
    }
}
