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
        BODY = new TableCellStyle(new HSSFColor.WHITE());
        BODY.sethAlign(CellHAlign.CENTER);
        BODY.setvAlign(CellVAlign.CENTER);
        BODY.setBorder(CellStyleBorder.DEFAULT);

        HEADER = new TableCellStyle(new HSSFColor.GREY_25_PERCENT());
        FOOTER = new TableCellStyle(new HSSFColor.GREY_25_PERCENT());
        for (TableCellStyle tcs : Arrays.asList(HEADER, FOOTER)) {
            tcs.setBold(true);
            tcs.setFontSize(14);
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

    public CellStyle toCellStyle(Workbook wb, String cellFormat) {
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
            font.setFontHeight(fontSize.shortValue());
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

}
