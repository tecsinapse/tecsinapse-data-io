/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class SpreadsheetCellStyle {

    private HSSFColor backgroundColor;

    public SpreadsheetCellStyle(HSSFColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public CellStyle toCellStyle(CellStyle cellStyle) {
        if (backgroundColor != null) {
            cellStyle.setFillForegroundColor(backgroundColor.getIndex());
            return cellStyle;
        }
        return cellStyle;
    }

    public HSSFColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(HSSFColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getCssStyle() {
        if (backgroundColor != null) {
            short[] rgb = backgroundColor.getTriplet();
            return String.format("background-color: #%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
        }
        return null;
    }

}
