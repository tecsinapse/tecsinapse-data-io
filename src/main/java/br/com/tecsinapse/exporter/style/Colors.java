/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.AUTOMATIC;
import org.apache.poi.hssf.util.HSSFColor.BLACK;
import org.apache.poi.hssf.util.HSSFColor.BLUE;
import org.apache.poi.hssf.util.HSSFColor.BRIGHT_GREEN;
import org.apache.poi.hssf.util.HSSFColor.BROWN;
import org.apache.poi.hssf.util.HSSFColor.DARK_BLUE;
import org.apache.poi.hssf.util.HSSFColor.DARK_GREEN;
import org.apache.poi.hssf.util.HSSFColor.DARK_RED;
import org.apache.poi.hssf.util.HSSFColor.DARK_YELLOW;
import org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.GREY_40_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.GREY_50_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.GREY_80_PERCENT;
import org.apache.poi.hssf.util.HSSFColor.ORANGE;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.hssf.util.HSSFColor.WHITE;
import org.apache.poi.hssf.util.HSSFColor.YELLOW;

public interface Colors {

    HSSFColor AUTOMATIC = new AUTOMATIC();
    HSSFColor BLACK = new BLACK();
    HSSFColor BLUE = new BLUE();
    HSSFColor BRIGHT_GREEN = new BRIGHT_GREEN();
    HSSFColor BROWN = new BROWN();
    HSSFColor DARK_BLUE = new DARK_BLUE();
    HSSFColor DARK_GREEN = new DARK_GREEN();
    HSSFColor DARK_RED = new DARK_RED();
    HSSFColor DARK_YELLOW = new DARK_YELLOW();
    HSSFColor GREY_25_PERCENT = new GREY_25_PERCENT();
    HSSFColor GREY_40_PERCENT = new GREY_40_PERCENT();
    HSSFColor GREY_50_PERCENT = new GREY_50_PERCENT();
    HSSFColor GREY_80_PERCENT = new GREY_80_PERCENT();
    HSSFColor ORANGE = new ORANGE();
    HSSFColor RED = new RED();
    HSSFColor WHITE = new WHITE();
    HSSFColor YELLOW = new YELLOW();
}
