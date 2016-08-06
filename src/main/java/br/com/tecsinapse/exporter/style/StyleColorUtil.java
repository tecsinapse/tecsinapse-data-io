/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.apache.poi.hssf.util.HSSFColor;

public final class StyleColorUtil {

    private StyleColorUtil() {
    }

    public static String toHexColor(HSSFColor hssfColor) {
        short[] rgb = hssfColor.getTriplet();
        return String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
    }
}
