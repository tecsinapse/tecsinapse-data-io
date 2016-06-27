/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import com.google.common.primitives.Doubles;

public class TableCell {

    private static final int COLUMN_WIDTH = 256;
    private String content = "";
    private Integer colspan = 1;
    private Integer rowspan = 1;
    private TableCellType tableCellType = TableCellType.BODY;
    private CellType cellType = CellType.STRING_TYPE;
    private String style;
    private String styleClass;
    private boolean bold = false;

    public TableCell() {
        super();
    }

    public TableCell(String content) {
        this();
        this.content = content;
    }

    public TableCell(String content, boolean bold) {
        this();
        this.content = content;
        this.bold = bold;
    }

    public TableCell(String content, String style) {
        this(content);
        this.style = style;
    }

    public TableCell(String content, String style, int colspan) {
        this(content);
        this.style = style;
        this.colspan = colspan;
    }

    public TableCell(String content, String style, int colspan, int rowspan) {
        this(content, colspan);
        this.style = style;
        this.rowspan = rowspan;
    }

    public TableCell(String content, int colspan) {
        this(content);
        this.colspan = colspan;
    }

    public TableCell(String content, int colspan, int rowspan) {
        this(content, colspan);
        this.rowspan = rowspan;
    }

    public TableCell(Number content) {
        this(content != null ? content.toString() : null);
        this.cellType = CellType.NUMERIC_TYPE;
    }

    public TableCell(Number content, boolean bold) {
        this(content != null ? content.toString() : null);
        this.cellType = CellType.NUMERIC_TYPE;
        this.bold = bold;
    }

    public TableCell(Number content, TableCellType tableCellType) {
        this(content);
        this.tableCellType = tableCellType;
    }

    public TableCell(Number content, TableCellType tableCellType, boolean bold) {
        this(content);
        this.tableCellType = tableCellType;
        this.bold = bold;
    }

    public TableCell(String content, CellType cellType) {
        this(content);
        this.cellType = cellType;
    }

    public TableCell(String content, TableCellType tableCellType) {
        this(content);
        this.tableCellType = tableCellType;
    }

    public TableCell(String content, TableCellType tableCellType, boolean bold) {
        this(content);
        this.tableCellType = tableCellType;
        this.bold = bold;
    }

    public TableCell(String content, TableCellType tableCellType, int colspan) {
        this(content, tableCellType);
        this.colspan = colspan;
    }

    public TableCell(String content, TableCellType tableCellType, int colspan, int rowspan) {
        this(content, tableCellType, colspan);
        this.rowspan = rowspan;
    }

    public TableCell(String content, TableCellType tableCellType, String style) {
        this(content);
        this.style = style;
        this.tableCellType = tableCellType;
    }

    public TableCell(String content, TableCellType tableCellType, String style, int colspan) {
        this(content, tableCellType);
        this.style = style;
        this.colspan = colspan;
    }

    public TableCell(String content, TableCellType tableCellType, String style, int colspan, int rowspan) {
        this(content, tableCellType, colspan);
        this.style = style;
        this.rowspan = rowspan;
    }

    int getDefaultColumnWidth() {
        return content == null || content.trim().length() == 0 ? 0 : content.length() * COLUMN_WIDTH;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public TableCellType getTableCellType() {
        return tableCellType;
    }

    public void setTableCellType(TableCellType tableCellType) {
        this.tableCellType = tableCellType;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public Double getContentAsDoubleOrNull() {
        return content == null ? null : Doubles.tryParse(content);
    }

    public String getStyle() {
        if (style == null && styleClass == null) {
            return getTableCellType().getDefaultStyle();
        }
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
