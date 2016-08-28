/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import com.google.common.primitives.Doubles;

import br.com.tecsinapse.exporter.style.TableCellStyle;
import br.com.tecsinapse.exporter.type.CellType;

public class TableCell {

    private static final int COLUMN_WIDTH = 256;
    private Object content = "";
    private Integer colspan = 1;
    private Integer rowspan = 1;
    private CellType cellType = CellType.STRING_TYPE;
    private boolean userDefinedCellType = false;
    private TableCellStyle tableCellStyle = TableCellStyle.BODY;
    private String style;
    private String styleClass;
    private ExporterFormatter exporterFormatter;

    public TableCell() {

    }

    @Deprecated
    public TableCell(String content, boolean bold) {
        this(content);
        setBold(bold);
    }

    @Deprecated
    public TableCell(String content, String style) {
        this(content);
        setStyle(style);
    }

    @Deprecated
    public TableCell(String content, String style, int colspan) {
        this(content);
        setStyle(style);
        setColspan(colspan);
    }

    @Deprecated
    public TableCell(String content, String style, int colspan, int rowspan) {
        this(content);
        setStyle(style);
        setColspan(colspan);
        setRowspan(rowspan);
    }

    @Deprecated
    public TableCell(String content, int colspan) {
        this(content);
        setColspan(colspan);
    }

    @Deprecated
    public TableCell(String content, int colspan, int rowspan) {
        this(content);
        setRowspan(rowspan);
        setColspan(colspan);
    }

    @Deprecated
    public TableCell(Number content, boolean bold) {
        this(content);
        setBold(bold);
    }

    @Deprecated
    public TableCell(Number content, TableCellType tableCellType, boolean bold) {
        this(content, tableCellType);
        setBold(bold);
    }

    @Deprecated
    public TableCell(String content, CellType cellType) {
        this(content);
        setCellType(cellType);
    }

    @Deprecated
    public TableCell(String content, TableCellType tableCellType, boolean bold) {
        this(content, tableCellType);
        setBold(bold);
    }

    @Deprecated
    public TableCell(String content, TableCellType tableCellType, int colspan) {
        this(content, tableCellType);
        setColspan(colspan);
    }

    @Deprecated
    public TableCell(String content, TableCellType tableCellType, int colspan, int rowspan) {
        this(content, tableCellType);
        setColspan(colspan);
        setRowspan(rowspan);
    }

    @Deprecated
    public TableCell(String content, TableCellType tableCellType, String style) {
        this(content, tableCellType);
        setStyle(style);
    }

    @Deprecated
    public TableCell(String content, TableCellType tableCellType, String style, int colspan) {
        this(content, tableCellType);
        setStyle(style);
        setColspan(colspan);
    }

    @Deprecated
    public TableCell(String content, TableCellType tableCellType, String style, int colspan, int rowspan) {
        this(content, tableCellType);
        setStyle(style);
        setColspan(colspan);
        setRowspan(rowspan);
    }

    @Deprecated
    public TableCell(Object content, TableCellType tableCellType) {
        this(content);
        setTableCellType(tableCellType);
    }

    public TableCell(Object content) {
        this.content = content;
        this.cellType = CellType.byObject(content);
    }

    public TableCell(Object content, CellType cellType) {
        this.content = content;
        setCellType(cellType);
    }

    public TableCell(Object content, TableCellStyle tableCellStyle) {
        this(content);
        setTableCellStyle(tableCellStyle);
    }

    public int getDefaultColumnWidth() {
        String value = getContent();
        return value == null || value.trim().length() == 0 ? 0 : value.length() * COLUMN_WIDTH;
    }

    public Object getContentObject() {
        return content;
    }

    public String getContent() {
        return getFormattedContent(exporterFormatter);
    }

    public String getFormattedContentInternalFirst(ExporterFormatter externalExporterFormatter) {
        return getFormattedContent(exporterFormatter == null ? externalExporterFormatter : exporterFormatter);
    }

    public String getFormattedContent(ExporterFormatter exporterFormatter) {
        if (content == null) {
            return null;
        }
        if (exporterFormatter != null) {
            return exporterFormatter.formatByType(content, cellType == CellType.CURRENCY_TYPE);
        }
        return content.toString();
    }

    public void setContent(Object content) {
        this.content = content;
        if (!userDefinedCellType) {
            this.cellType = CellType.byObject(content);
        }
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

    @Deprecated
    public boolean isBold() {
        return tableCellStyle != null ? tableCellStyle.isBold() : false;
    }

    @Deprecated
    public void setBold(boolean bold) {
        if (tableCellStyle == null) {
            tableCellStyle = TableCellStyle.BODY;
        }
        tableCellStyle.setBold(bold);
    }

    @Deprecated
    public void setTableCellType(TableCellType tableCellType) {
        if (tableCellType == TableCellType.BODY) {
            setTableCellStyle(TableCellStyle.BODY);
            return;
        }
        if (tableCellType == TableCellType.HEADER) {
            setTableCellStyle(TableCellStyle.HEADER);
            return;
        }
        if (tableCellType == TableCellType.FOOTER) {
            setTableCellStyle(TableCellStyle.FOOTER);
            return;
        }
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.userDefinedCellType = true;
        this.cellType = cellType;
    }

    public Double getContentAsDoubleOrNull() {
        if (content instanceof Number) {
            return ((Number) content).doubleValue();
        }
        return content == null ? null : Doubles.tryParse(getContent());
    }

    public String getStyle() {
        if (style == null && styleClass == null && tableCellStyle != null) {
            return tableCellStyle.getCssStyle();
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

    public TableCellStyle getTableCellStyle() {
        return tableCellStyle;
    }

    public void setTableCellStyle(TableCellStyle tableCellStyle) {
        this.tableCellStyle = tableCellStyle;
    }

    public ExporterFormatter getExporterFormatter() {
        return exporterFormatter;
    }

    public void setExporterFormatter(ExporterFormatter exporterFormatter) {
        this.exporterFormatter = exporterFormatter;
    }
}
