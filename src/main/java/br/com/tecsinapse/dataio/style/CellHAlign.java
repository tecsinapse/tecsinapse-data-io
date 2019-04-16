/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CellHAlign {

    CENTER(HorizontalAlignment.CENTER, "text-align:center;"),
    JUSTIFY(HorizontalAlignment.JUSTIFY, "text-align:justify;"),
    LEFT(HorizontalAlignment.LEFT, "text-align:left;"),
    RIGHT(HorizontalAlignment.RIGHT, "text-align:right;");

    private final HorizontalAlignment cellStyleHAlign;
    private final String css;

}
