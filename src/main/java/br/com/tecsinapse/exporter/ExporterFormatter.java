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
import org.joda.time.format.DateTimeFormat;

import br.com.tecsinapse.exporter.util.Constants;

public class ExporterFormatter {

    public static final ExporterFormatter PT_BR = new ExporterFormatter(Constants.LOCALE_PT_BR);
    public static final ExporterFormatter DEFAULT = new ExporterFormatter(Locale.ENGLISH);

    private final String localDateTimeFormat;
    private final String localDateFormat;
    private final String localTimeFormat;
    private final DecimalFormat decimalFormat;
    private final DecimalFormat integerFormat;
    private final DecimalFormat currencyFormat;
    private final Locale locale;

    public ExporterFormatter(Locale locale) {
        this.locale = locale;
        this.localDateTimeFormat = DateTimeFormat.patternForStyle("MM", locale);
        this.localDateFormat = DateTimeFormat.patternForStyle("M-", locale);
        this.localTimeFormat = DateTimeFormat.patternForStyle("-S", locale);
        this.decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        this.integerFormat = (DecimalFormat) DecimalFormat.getIntegerInstance(locale);
        this.currencyFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
    }

    public ExporterFormatter(String localDateTimeFormat, String localDateFormat, String localTimeFormat, String decimalFormat, String integerFormat, String currencyFormat) {
        this(localDateTimeFormat, localDateFormat, localTimeFormat, decimalFormat, integerFormat, currencyFormat, Locale.getDefault());
    }

    public ExporterFormatter(String localDateTimeFormat, String localDateFormat, String localTimeFormat, String decimalFormat, String integerFormat, String currencyFormat, Locale locale) {
        this.locale = locale;
        this.localDateTimeFormat = localDateTimeFormat;
        this.localDateFormat = localDateFormat;
        this.localTimeFormat = localTimeFormat;
        this.decimalFormat = new DecimalFormat(decimalFormat, DecimalFormatSymbols.getInstance(locale));
        this.integerFormat = new DecimalFormat(integerFormat, DecimalFormatSymbols.getInstance(locale));
        this.currencyFormat = new DecimalFormat(currencyFormat, DecimalFormatSymbols.getInstance(locale));
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

    public String getIntegerFormat() {
        return integerFormat.toPattern();
    }

    public String getCurrencyFormat() {
        return currencyFormat.toPattern();
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

    public String formatCurrency(Number number) {
        return currencyFormat.format(number);
    }

    public String formatByType(Object o, boolean isCurrency) {
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
            if (isCurrency) {
                return formatCurrency((Number) o);
            }
            return formatNumber((Number) o);
        }
        return o.toString();
    }

    public String getStringFormatByType(Object o, boolean isCurrency) {
        String pattern = getStringByType(o, isCurrency);
        if (pattern == null) {
            return null;
        }
        if (o instanceof Number) {
            return pattern;
        }
        return DateFormatConverter.convert(locale, pattern);
    }

    private String getStringByType(Object o, boolean isCurrency) {
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
        if (o instanceof Number && isCurrency) {
            return currencyFormat.toPattern();
        }
        if (o instanceof Number) {
            return decimalFormat.toPattern();
        }
        return null;
    }

}
