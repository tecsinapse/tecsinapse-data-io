/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.datasources;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.BLACK;
import org.apache.poi.hssf.util.HSSFColor.BLUE;
import org.apache.poi.hssf.util.HSSFColor.BRIGHT_GREEN;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.hssf.util.HSSFColor.WHITE;

public interface HSSFColorDs {
    HSSFColor BLACK = new BLACK();
    HSSFColor WHITE = new WHITE();
    HSSFColor RED = new RED();
    HSSFColor BLUE = new BLUE();
    HSSFColor GREEN = new BRIGHT_GREEN();
}
