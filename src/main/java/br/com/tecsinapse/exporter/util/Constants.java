/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.util;

import java.util.Locale;

import org.apache.poi.ss.usermodel.DateUtil;
import org.joda.time.LocalDate;

public interface Constants {
    short DECIMAL_PRECISION = 10;
    LocalDate LOCAL_DATE_BIGBANG = LocalDate.fromDateFields(DateUtil.getJavaDate(0.0, true));
    Locale LOCALE_PT_BR = new Locale("pt", "BR");
    Locale LOCALE_ES_ES = new Locale("es", "ES");
}
