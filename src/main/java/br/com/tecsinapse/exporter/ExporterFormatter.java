/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.poi.ss.util.DateFormatConverter;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class ExporterFormatter {

    public static final ExporterFormatter PT_BR = new ExporterFormatter("dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy", "HH:mm", "#,###,###.##", "#,###,###", new Locale("pt", "BR"));
    public static final ExporterFormatter DEFAULT = new ExporterFormatter("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm", "#,###,###.##",  "#,###,###", Locale.ENGLISH);

    private final String localDateTimeFormat;
    private final String localDateFormat;
    private final String localTimeFormat;
    private final DecimalFormat decimalFormat;
    private final DecimalFormat integerFormat;
    private final Locale locale;

    public ExporterFormatter(String localDateTimeFormat, String localDateFormat, String localTimeFormat, String decimalFormat, String integerFormat) {
        this(localDateTimeFormat, localDateFormat, localTimeFormat, decimalFormat, integerFormat, Locale.getDefault());
    }

    public ExporterFormatter(String localDateTimeFormat, String localDateFormat, String localTimeFormat, String decimalFormat, String integerFormat, Locale locale) {
        this.locale = locale;
        this.localDateTimeFormat = localDateTimeFormat;
        this.localDateFormat = localDateFormat;
        this.localTimeFormat = localTimeFormat;
        this.decimalFormat = new DecimalFormat(decimalFormat, DecimalFormatSymbols.getInstance(locale));
        this.integerFormat = new DecimalFormat(integerFormat, DecimalFormatSymbols.getInstance(locale));
    }

    public String getLocalDateTimeFormat() {
        return localDateTimeFormat;
    }

    public String getLocalDateFormat() {
        return localDateFormat;
    }

    public String getLocalTimeFormat() {
        return localTimeFormat;
    }

    public String getDecimalFormater() {
        return decimalFormat.toPattern();
    }

    public DecimalFormat getIntegerFormat() {
        return integerFormat;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String formatLocalDate(LocalDate localDate) {
        return localDate.toString(localDateFormat);
    }

    public String formatLocalTime(LocalTime localTime) {
        return localTime.toString(localTimeFormat);
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toString(localDateTimeFormat);
    }

    public String formatNumber(Number number) {
        if (number instanceof Integer || number instanceof Long) {
            return integerFormat.format(number);
        }
        return decimalFormat.format(number);
    }

    public String formatByType(Object o) {
        if (o instanceof LocalDateTime) {
            return formatLocalDateTime((LocalDateTime) o);
        }
        if (o instanceof LocalDate) {
            return formatLocalDate((LocalDate) o);
        }
        if (o instanceof LocalTime) {
            return formatLocalTime((LocalTime) o);
        }
        if (o instanceof Number) {
            return formatNumber((Number) o);
        }
        return o.toString();
    }

    public String getStringFormatByType(Object o) {
        String pattern = getStringByType(o);
        if (pattern == null) {
            return null;
        }
        return DateFormatConverter.convert(locale, pattern);
    }

    private String getStringByType(Object o) {
        if (o instanceof LocalDateTime) {
            return localDateTimeFormat;
        }
        if (o instanceof LocalDate) {
            return localDateFormat;
        }
        if (o instanceof LocalTime) {
            return localTimeFormat;
        }
        if (o instanceof Integer || o instanceof Long) {
            return integerFormat.toPattern();
        }
        if (o instanceof Number) {
            return decimalFormat.toPattern();
        }
        return null;
    }



}
