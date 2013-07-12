package br.com.tecsinapse.exporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

public class Table {
	private String title;
	List<List<TableCell>> cells = new ArrayList<List<TableCell>>();

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

	public void addOnNewRow(TableCell cell) {
		addNewRow();
		add(cell);
	}

	public void addNewRow() {
		cells.add(new ArrayList<TableCell>());
	}

	public List<List<TableCell>> getCells() {
		return cells;
	}

	public void setCells(List<List<TableCell>> cells) {
		this.cells = cells;
	}

	public List<List<String>> toStringMatrix() {
		int rows = cells.size();
		int columns = getBiggerRowSize();

		List<List<String>> matrix = new ArrayList<List<String>>();

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

		List<List<TableCell>> matrix = new ArrayList<List<TableCell>>();

		boolean[][] spanMark = new boolean[rows][];
		for (int r = 0; r < rows; ++r) {
			spanMark[r] = new boolean[columns];
			matrix.add(new ArrayList<TableCell>());
			for (int c = 0; c < columns; ++c) {
				matrix.get(r).add(new TableCell(""));
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
					System.out.print(cell.getContent() + " - ");

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

	private int getBiggerRowSize() {
		int biggerRowSize = 0;
		for (List<TableCell> row : cells) {
			if (row.size() > biggerRowSize) {
				biggerRowSize = row.size();

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

	public void printString() {

		for (List<TableCell> row : cells) {
			for (TableCell cell : row) {
				System.out.print("|" + cell.getContent());
			}
			System.out.println();
		}

	}

	public Workbook toWorkBook() {
		List<List<TableCell>> matrix = cells;
		List<List<TableCell>> matrixFull = toTableCellMatrix();

		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		int titleRows = 0;
		int r = titleRows;
		int c = 0;
		int maxColumns = -1;

		CellStyle styleHeader = header(getDefaultCellStyle(wb));
		CellStyle styleBody = body(getDefaultCellStyle(wb));
		CellStyle styleFooter = footer(getDefaultCellStyle(wb));

		for (List<TableCell> row : matrix) {
			Row sheetRow = sheet.createRow(r);
			for (TableCell tableCell : row) {
				while (matrixFull.get(r - titleRows).get(c).getContent()
						.equals("")) {
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
					sheet.addMergedRegion(new CellRangeAddress(rowStart,
							rowEnd, colStart, colEnd));
				}

				try {
					Double value = Double.valueOf(tableCell.getContent());
					cell.setCellValue(value);
				} catch (Exception e) {
					cell.setCellValue(tableCell.getContent());
				}
				switch (tableCell.getTableCellType()) {
				case HEADER:
					cell.setCellStyle(styleHeader);
					break;
				case BODY:
					cell.setCellStyle(styleBody);
					break;
				case FOOTER:
					cell.setCellStyle(styleFooter);
					break;
				}
				c++;
			}
			r++;
			c = 0;
		}
		
		for(int i = 0 ; i <= maxColumns ; ++i) {
			sheet.autoSizeColumn(i, true);
		}
		
		return wb;
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
			List<TableCell> emptyCells = new ArrayList<TableCell>();
			for (int i = 0; i < biggerRow; ++i) {
				emptyCells.add(new TableCell(""));
			}
			for (int i = 0; i < rowsOut - rows; ++i) {
				this.addNewRow();
				this.addAllCells(emptyCells);
			}
		}

		for (List<TableCell> row : this.cells) {
			row.add(new TableCell(""));
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
		List<TableCell> emptyCells = new ArrayList<TableCell>();
		for (int i = 0; i < biggerRow; ++i) {
			emptyCells.add(new TableCell(""));
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

}
