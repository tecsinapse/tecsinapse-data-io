/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.util;

import java.util.Locale;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {

    public static final short DECIMAL_PRECISION = 10;
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");
    public static final Locale LOCALE_ES_ES = new Locale("es", "ES");
    public static final String MSG_IGNORED = "Ignored";
    public static final String MIME_XLS = "application/vnd.ms-excel";
    public static final String MIME_XLSM = "application/vnd.ms-excel.sheet.macroEnabled.12";
    public static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

}
