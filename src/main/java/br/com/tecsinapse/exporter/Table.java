/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.tecsinapse.exporter.importer.ParserFormatter;

public class Table {

    private ParserFormatter parserFormatter = ParserFormatter.DEFAULT;

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
        if (cells.size() > 0) {
            cells.remove(0);
        }
    }

    public void removeInitialRows(int numberRows) {
        if (cells.size() > numberRows) {
            for (int i = 0; i < numberRows; i++) {
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
                    matrix.get(r).set(c, cell.getContent(getParserFormatter()));

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
            for (TableCell column : row) {
                qtdColumns += column.getColspan();
                if (qtdColumns > biggerRowSize) {
                    biggerRowSize = qtdColumns;
                }
            }
        }
        return biggerRowSize;
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

    public Workbook toXSSFWorkBook() {
        return toWorkBook(new XSSFWorkbook());
    }

    public Workbook toHSSFWorkBook() {
        return toWorkBook(new HSSFWorkbook());
    }

    public Workbook toWorkBook(Workbook wb) {
        return WorkbookUtil.toWorkBook(wb, this);
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

    public ParserFormatter getParserFormatter() {
        return parserFormatter;
    }

    public void setParserFormatter(ParserFormatter parserFormatter) {
        this.parserFormatter = parserFormatter;
    }
}
