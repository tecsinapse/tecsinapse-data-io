/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.apache.poi.hssf.util.HSSFColor;

final class TableCellStyleDefaultHeaderFooter extends TableCellStyle {

    public TableCellStyleDefaultHeaderFooter() {
        super(Colors.GREY_25_PERCENT, "header", true);
        super.setBold(true);
        super.setFontSize(10);
        super.sethAlign(CellHAlign.CENTER);
        super.setvAlign(CellVAlign.CENTER);
    }

    private void throwUnsupportedOperationException() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("TableCellStyleDefaultHeaderFooter: Unsupported operation for default objects.");
    }


    @Override
    public void setFontColor(HSSFColor fontColor) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setBackgroundColor(HSSFColor backgroundColor) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setFontSize(Integer fontSize) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setBold(boolean bold) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setItalic(boolean italic) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setUnderline(boolean underline) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setStrikeout(boolean strikeout) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setvAlign(CellVAlign vAlign) {
        throwUnsupportedOperationException();
    }

    @Override
    public void sethAlign(CellHAlign hAlign) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setBorder(CellStyleBorder border) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setCellFormat(String cellFormat) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setCssWhiteAsTransparent(boolean cssWhiteAsTransparent) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setCssClass(String cssClass) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setIgnoreCssStyle(boolean ignoreCssStyle) {
        throwUnsupportedOperationException();
    }
}
