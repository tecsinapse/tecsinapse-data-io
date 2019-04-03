/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

public enum CellHAlign {
    CENTER(CellStyle.ALIGN_CENTER, "text-align:center;"),
    JUSTIFY(CellStyle.ALIGN_JUSTIFY, "text-align:justify;"),
    LEFT(CellStyle.ALIGN_LEFT, "text-align:left;"),
    RIGHT(CellStyle.ALIGN_RIGHT, "text-align:right;");

    CENTER(HorizontalAlignment.CENTER, "text-align:center;"),
    JUSTIFY(HorizontalAlignment.JUSTIFY, "text-align:justify;"),
    LEFT(HorizontalAlignment.LEFT, "text-align:left;"),
    RIGHT(HorizontalAlignment.RIGHT, "text-align:right;");

    private final HorizontalAlignment cellStyleHAlign;
    private final String css;

    CellHAlign(short cellStyleHAlign, String css) {
        this.cellStyleHAlign = cellStyleHAlign;
        this.css = css;
    }

    public short getCellStyleHAlign() {
        return cellStyleHAlign;
    }

    public String getCss() {
        return css;
    }
}
