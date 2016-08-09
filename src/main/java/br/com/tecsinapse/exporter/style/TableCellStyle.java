/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.style;

import java.util.Arrays;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class TableCellStyle implements Cloneable {

    public static final TableCellStyle HEADER;
    public static final TableCellStyle BODY;
    public static final TableCellStyle FOOTER;

    static {
        BODY = new TableCellStyle(Colors.WHITE);
        BODY.sethAlign(CellHAlign.CENTER);
        BODY.setvAlign(CellVAlign.CENTER);
        BODY.setBorder(CellStyleBorder.DEFAULT);
        BODY.setFontSize(10);

        HEADER = new TableCellStyle(Colors.GREY_25_PERCENT);
        FOOTER = new TableCellStyle(Colors.GREY_25_PERCENT);
        for (TableCellStyle tcs : Arrays.asList(HEADER, FOOTER)) {
            tcs.setBold(true);
            tcs.setFontSize(10);
            tcs.sethAlign(CellHAlign.CENTER);
            tcs.setvAlign(CellVAlign.CENTER);
            tcs.setBorder(CellStyleBorder.DEFAULT);
        }
    }

    private HSSFColor backgroundColor;
    private HSSFColor fontColor;
    private CellVAlign vAlign;
    private CellHAlign hAlign;
    private CellStyleBorder border;
    private Integer fontSize;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean strikeout = false;
    private String cellFormat;

    public TableCellStyle(HSSFColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public HSSFColor getBackgroundColor() {
        return backgroundColor;
    }

    public HSSFColor getFontColor() {
        return fontColor;
    }

    public void setFontColor(HSSFColor fontColor) {
        this.fontColor = fontColor;
    }

    public void setBackgroundColor(HSSFColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isStrikeout() {
        return strikeout;
    }

    public void setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
    }

    public CellVAlign getvAlign() {
        return vAlign;
    }

    public void setvAlign(CellVAlign vAlign) {
        this.vAlign = vAlign;
    }

    public CellHAlign gethAlign() {
        return hAlign;
    }

    public void sethAlign(CellHAlign hAlign) {
        this.hAlign = hAlign;
    }

    public CellStyleBorder getBorder() {
        return border;
    }

    public void setBorder(CellStyleBorder border) {
        this.border = border;
    }

    public String getCellFormat() {
        return cellFormat;
    }

    public void setCellFormat(String cellFormat) {
        this.cellFormat = cellFormat;
    }

    public String getCssStyle() {
        StringBuilder css = new StringBuilder();
        if (getBackgroundColor() != null) {
            css.append(CssStyle.toBackgroundColor(backgroundColor));
        }
        if (getBorder() != null) {
            css.append(border.toCss());
        }
        if (getvAlign() != null) {
            css.append(vAlign.getCss());
        }
        if (gethAlign() != null) {
            css.append(hAlign.getCss());
        }
        configCssFont(css);
        return css.length() > 0 ? css.toString() : null;
    }

    public CellStyle toCellStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        if (getBackgroundColor() != null) {
            cellStyle.setFillForegroundColor(getBackgroundColor().getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        if (getBorder() != null) {
            cellStyle = border.toCellStyle(cellStyle);
        }
        if (getvAlign() != null) {
            cellStyle.setVerticalAlignment(vAlign.getCellStyleVAlign());
        }
        if (gethAlign() != null) {
            cellStyle.setAlignment(hAlign.getCellStyleHAlign());
        }
        configFont(font);
        cellStyle.setFont(font);
        if (cellFormat != null && !cellFormat.isEmpty()) {
            cellStyle.setDataFormat(wb.createDataFormat().getFormat(cellFormat));
        }
        return cellStyle;
    }

    private void configFont(Font font) {
        font.setBold(isBold());
        font.setItalic(isItalic());
        font.setStrikeout(isStrikeout());
        font.setUnderline(isUnderline() ? Font.U_SINGLE : Font.U_NONE);
        if (getFontSize() != null) {
            font.setFontHeightInPoints(fontSize.shortValue());
        }
        if (getFontColor() != null) {
            font.setColor(fontColor.getIndex());
        }
    }

    private void configCssFont(StringBuilder builder) {
        if (isBold()) {
            builder.append(CssStyle.BOLD.getCss());
        }
        if (isItalic()) {
            builder.append(CssStyle.ITALIC.getCss());
        }
        if (isStrikeout()) {
            builder.append(CssStyle.STRIKEOUT.getCss());
        }
        if (isUnderline()) {
            builder.append(CssStyle.UNDERLINE.getCss());
        }
        if (getFontSize() != null) {
            builder.append(CssStyle.toFontSize(fontSize));
        }
        if (getFontColor() != null) {
            builder.append(CssStyle.toTextColor(fontColor));
        }
    }

    public TableCellStyle clone() {
        TableCellStyle tcs = new TableCellStyle(getBackgroundColor());
        CellStyleBorder csb = getBorder();
        if (csb != null) {
            tcs.setBorder(csb == CellStyleBorder.DEFAULT ? csb : csb.clone());
        }

        tcs.setvAlign(getvAlign());
        tcs.sethAlign(gethAlign());

        tcs.setFontColor(getFontColor());
        tcs.setFontSize(getFontSize());

        tcs.setBold(isBold());
        tcs.setStrikeout(isStrikeout());
        tcs.setItalic(isItalic());
        tcs.setUnderline(isUnderline());
        return tcs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof TableCellStyle)) {
            return false;
        }

        final TableCellStyle that = (TableCellStyle) o;

        if (bold != that.bold) {
            return false;
        }
        if (italic != that.italic) {
            return false;
        }
        if (underline != that.underline) {
            return false;
        }
        if (strikeout != that.strikeout) {
            return false;
        }
        if (backgroundColor != null ? !backgroundColor.equals(that.backgroundColor) : that.backgroundColor != null) {
            return false;
        }
        if (fontColor != null ? !fontColor.equals(that.fontColor) : that.fontColor != null) {
            return false;
        }
        if (vAlign != that.vAlign) {
            return false;
        }
        if (hAlign != that.hAlign) {
            return false;
        }
        if (border != null ? !border.equals(that.border) : that.border != null) {
            return false;
        }
        if (cellFormat != null ? !cellFormat.equals(that.cellFormat) : that.cellFormat != null) {
            return false;
        }
        return fontSize != null ? fontSize.equals(that.fontSize) : that.fontSize == null;

    }

    @Override
    public int hashCode() {
        int result = backgroundColor != null ? backgroundColor.hashCode() : 0;
        result = 31 * result + (fontColor != null ? fontColor.hashCode() : 0);
        result = 31 * result + (vAlign != null ? vAlign.hashCode() : 0);
        result = 31 * result + (hAlign != null ? hAlign.hashCode() : 0);
        result = 31 * result + (border != null ? border.hashCode() : 0);
        result = 31 * result + (fontSize != null ? fontSize.hashCode() : 0);
        result = 31 * result + (bold ? 1 : 0);
        result = 31 * result + (italic ? 1 : 0);
        result = 31 * result + (underline ? 1 : 0);
        result = 31 * result + (strikeout ? 1 : 0);
        return result;
    }
}
