/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import org.apache.poi.ss.usermodel.VerticalAlignment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CellVAlign {

    CENTER(VerticalAlignment.CENTER, "vertical-align:middle;"),
    JUSTIFY(VerticalAlignment.JUSTIFY, ""),
    TOP(VerticalAlignment.TOP, "vertical-align:top;"),
    BOTTOM(VerticalAlignment.BOTTOM, "vertical-align:bottom;");

    private final VerticalAlignment cellStyleVAlign;
    private final String css;

}
