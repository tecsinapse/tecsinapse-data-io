/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.style;

import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_BODY;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_BODY_BOLD;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_BODY_CENTER;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_BODY_CENTER_BOLD;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_FOOTER;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_FOOTER_BOLD;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_HEADER;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_HEADER_BOLD;
import static br.com.tecsinapse.dataio.util.WorkbookUtil.toIndexedColorMap;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TableCellStyle {

    private HSSFColor backgroundColor;
    private HSSFColor fontColor;
    private CellVAlign vAlign;
    private CellHAlign hAlign;
    private CellStyleBorder border = Style.CELL_STYLE_BORDER_DEFAULT;
    private Integer fontSize;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean strikeout = false;
    private boolean wrapText = false;
    private String cellFormat;
    private boolean cssWhiteAsTransparent = true;
    private boolean ignoreCssStyle;
    private String cssClass;

    public TableCellStyle(HSSFColor backgroundColor, String cssClass, boolean ignoreCssStyle) {
        this.backgroundColor = backgroundColor;
        this.ignoreCssStyle = ignoreCssStyle;
        this.cssClass = cssClass;
        this.fontSize = 10;
    }

    public TableCellStyle(HSSFColor backgroundColor) {
        this(backgroundColor, null, false);
    }

    public boolean isHeader() {
        return this.equals(TABLE_CELL_STYLE_HEADER) || this.equals(TABLE_CELL_STYLE_HEADER_BOLD);
    }

    public boolean isBody() {
        return this.equals(TABLE_CELL_STYLE_BODY) || this.equals(TABLE_CELL_STYLE_BODY_BOLD) ||
                this.equals(TABLE_CELL_STYLE_BODY_CENTER) || this.equals(TABLE_CELL_STYLE_BODY_CENTER_BOLD);
    }

    public boolean isFooter() {
        return this.equals(TABLE_CELL_STYLE_FOOTER) || this.equals(TABLE_CELL_STYLE_FOOTER_BOLD);
    }

    public String getCssStyle() {
        if (ignoreCssStyle) {
            return null;
        }
        StringBuilder css = new StringBuilder();
        if (getBackgroundColor() != null) {
            css.append(CssStyle.toBackgroundColor(backgroundColor, cssWhiteAsTransparent));
        }
        if (getBorder() != null) {
            css.append(border.toCss());
        }
        if (getVAlign() != null) {
            css.append(vAlign.getCss());
        }
        if (getHAlign() != null) {
            css.append(hAlign.getCss());
        }
        configCssFont(css);
        return css.length() > 0 ? css.toString() : null;
    }

    public CellStyle toCellStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();

        if (getBackgroundColor() != null) {
            if (cellStyle instanceof XSSFCellStyle) {
                ((XSSFCellStyle)cellStyle).setFillForegroundColor(new XSSFColor(toIndexedColorMap(getBackgroundColor())));
            } else {
                cellStyle.setFillForegroundColor(getBackgroundColor().getIndex());
            }
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        if (getBorder() != null) {
            cellStyle = border.toCellStyle(cellStyle);
        }
        if (getVAlign() != null) {
            cellStyle.setVerticalAlignment(vAlign.getCellStyleVAlign());
        }
        if (getHAlign() != null) {
            cellStyle.setAlignment(hAlign.getCellStyleHAlign());
        }

        cellStyle.setWrapText(isWrapText());

        Font font = wb.createFont();
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
            if (font instanceof XSSFFont) {
                ((XSSFFont)font).setColor(new XSSFColor(toIndexedColorMap(fontColor)));
            } else {
                font.setColor(fontColor.getIndex());
            }
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

    public TableCellStyle duplicate() {
        TableCellStyle tcs = new TableCellStyle(getBackgroundColor());
        CellStyleBorder csb = getBorder();
        if (csb != null) {
            tcs.setBorder(csb.duplicate());
        } else {
            tcs.setBorder(null);
        }

        tcs.setVAlign(getVAlign());
        tcs.setHAlign(getHAlign());

        tcs.setFontColor(getFontColor());
        tcs.setFontSize(getFontSize());

        tcs.setBold(isBold());
        tcs.setStrikeout(isStrikeout());
        tcs.setWrapText(isWrapText());
        tcs.setItalic(isItalic());
        tcs.setUnderline(isUnderline());
        tcs.setCssWhiteAsTransparent(isCssWhiteAsTransparent());
        tcs.setIgnoreCssStyle(isIgnoreCssStyle());
        tcs.setCssClass(getCssClass());
        return tcs;
    }

    public String getCssBlockClassName() {
        return String.format("tbcs-%s", cssClass != null
                ? cssClass
                : Integer.toHexString(System.identityHashCode(this)));
    }

    public String getCssBlock() {
        return String.format(".%s {%s}\n",
                getCssBlockClassName(),
                getCssStyle());
    }
}
