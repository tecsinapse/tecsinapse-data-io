/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class ExporterDateUtils {

    private static final Date DATETIME_BIGBANG = new Date(-2209064400000L);
    private static final Date DATETIME_BIGBANG_PLUS_24H = new Date(-2208977612000L);
    private static final SimpleDateFormat TIME_ISO_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat DATE_ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat DATETIME_FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm");

    private ExporterDateUtils() {

    }

    public static void main(String[] args) {
        System.out.println(DATETIME_BIGBANG.getTime());
        System.out.println(DATETIME_BIGBANG);
        System.out.println(DATETIME_BIGBANG_PLUS_24H.getTime());
        System.out.println(DATETIME_BIGBANG_PLUS_24H);
    }

    public static DateType getDateType(Date date) {
        if (date == null) {
            return DateType.NO_DATE;
        }
        if (date instanceof java.sql.Date) {
            date = new Date(date.getTime());
        }
        if (DATETIME_BIGBANG_PLUS_24H.after(date)) {
            return DateType.TIME;
        }
        if (date.getSeconds() == 0 && date.getMinutes() == 0 && date.getHours() == 0) {
            return DateType.DATE;
        }
        return DateType.DATETIME;
    }

    public static String formatWithIsoByDateType(Date date) {
        DateType dateType = getDateType(date);
        if (dateType == DateType.NO_DATE) {
            return null;
        }
        if (dateType == DateType.TIME) {
            return TIME_ISO_FORMAT.format(date);
        }
        if (dateType == DateType.DATE) {
            return DATE_ISO_FORMAT.format(date);
        }
        return DATETIME_ISO_FORMAT.format(date);
    }

    public static String formatAsFileDateTime(Date date) {
        return DATETIME_FILE_FORMAT.format(date);
    }

    public enum DateType {
        TIME, DATE, DATETIME, NO_DATE
    }

}
