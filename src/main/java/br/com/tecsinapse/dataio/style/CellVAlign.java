/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import org.apache.poi.ss.usermodel.CellStyle;

public enum CellVAlign {
    CENTER(CellStyle.VERTICAL_CENTER, "vertical-align:middle;"),
    JUSTIFY(CellStyle.VERTICAL_JUSTIFY, ""),
    TOP(CellStyle.VERTICAL_TOP, "vertical-align:top;"),
    BOTTOM(CellStyle.VERTICAL_BOTTOM, "vertical-align:bottom;");

    private final short cellStyleVAlign;
    private final String css;

    CellVAlign(short cellStyleVAlign, String css) {
        this.cellStyleVAlign = cellStyleVAlign;
        this.css = css;
    }

    public short getCellStyleVAlign() {
        return cellStyleVAlign;
    }

    public String getCss() {
        return css;
    }
}
