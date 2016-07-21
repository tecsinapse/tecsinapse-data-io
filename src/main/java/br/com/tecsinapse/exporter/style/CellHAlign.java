/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.apache.poi.ss.usermodel.CellStyle;

public enum CellHAlign {
    CENTER(CellStyle.ALIGN_CENTER, "text-align:center;"),
    JUSTIFY(CellStyle.ALIGN_JUSTIFY, "text-align:justify;"),
    LEFT(CellStyle.ALIGN_LEFT, "text-align:left;"),
    RIGHT(CellStyle.ALIGN_RIGHT, "text-align:right;");

    private final short cellStyleHAlign;
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
