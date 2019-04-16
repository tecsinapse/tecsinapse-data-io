/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public final class ExporterDateUtils {

    private static final Date DATETIME_BIGBANG_PLUS_24H = new Date(-2208977612000L);
    private static final String TIME_ISO_FORMAT = "HH:mm:ss";
    private static final String DATE_ISO_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATETIME_FILE_FORMAT = "yyyy-MM-dd_HH-mm";

    public static DateType getDateType(Date date) {
        if (date == null) {
            return DateType.NO_DATE;
        }
        if (date instanceof java.sql.Date) {
            return DateType.DATE;
        }
        if (DATETIME_BIGBANG_PLUS_24H.after(date)) {
            return DateType.TIME;
        }

        if (isTimeFieldEqualZero(date)) {
            return DateType.DATE;
        }
        return DateType.DATETIME;
    }

    private static boolean isTimeFieldEqualZero(Date date) {
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.HOUR) == 0;
    }

    public static String formatWithIsoByDateType(Date date) {
        DateType dateType = getDateType(date);
        if (dateType == DateType.NO_DATE) {
            return null;
        }
        if (dateType == DateType.TIME) {
            return formatDate(TIME_ISO_FORMAT, date);
        }
        if (dateType == DateType.DATE) {
            return formatDate(DATE_ISO_FORMAT, date);
        }
        return formatDate(DATETIME_ISO_FORMAT, date);
    }

    public static String formatAsFileDateTime(Date date) {
        return formatDate(DATETIME_FILE_FORMAT, date);
    }

    public static Date parseDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(DATETIME_ISO_FORMAT).parse(strDate);
        } catch (ParseException e) {
            log.error("Parse date '{}' error.", strDate, e);
            return null;
        }
    }

    public enum DateType {
        TIME, DATE, DATETIME, NO_DATE
    }

    private static String formatDate(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

}
