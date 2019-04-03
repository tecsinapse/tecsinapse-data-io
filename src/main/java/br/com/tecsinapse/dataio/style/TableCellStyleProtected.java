/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import org.apache.poi.hssf.util.HSSFColor;

class TableCellStyleProtected extends TableCellStyle {

    TableCellStyleProtected(HSSFColor backgroundColor, String cssClass, boolean ignoreCssStyle,
                            CellHAlign cellHAlign, CellVAlign cellVAlign, boolean bold) {
        super(backgroundColor, cssClass, ignoreCssStyle);
        super.sethAlign(cellHAlign);
        super.setvAlign(cellVAlign);
        super.setBold(bold);
    }

    private void throwUnsupportedOperationException() {
        throw new UnsupportedOperationException("TableCellStyleDefaultHeader: Unsupported operation for default objects.");
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
