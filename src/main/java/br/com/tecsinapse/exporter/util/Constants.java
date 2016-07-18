/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.util;

import org.apache.poi.ss.usermodel.DateUtil;
import org.joda.time.LocalDate;

public final class Constants {

    public static short DECIMAL_PRECISION = 10;
    public static final LocalDate LOCAL_DATE_BIGBANG = LocalDate.fromDateFields(DateUtil.getJavaDate(0.0, true));

}
