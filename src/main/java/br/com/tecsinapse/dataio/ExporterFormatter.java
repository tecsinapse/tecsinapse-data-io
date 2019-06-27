/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.ss.util.DateFormatConverter;

import br.com.tecsinapse.dataio.type.CellType;
import br.com.tecsinapse.dataio.util.Constants;
import br.com.tecsinapse.dataio.util.ExporterDateUtils;
import br.com.tecsinapse.dataio.util.ExporterDateUtils.DateType;

public class ExporterFormatter {

    public static final ExporterFormatter BRAZILIAN = new ExporterFormatter("dd/MM/yyyy H:mm:ss", "dd/MM/yyyy", "H:mm:ss", Constants.LOCALE_PT_BR);
    public static final ExporterFormatter ENGLISH = new ExporterFormatter("M/d/yyyy h:mm:ss a", "M/d/yyyy", "h:mm:ss a", Locale.ENGLISH);

    private static final String CURRENCY_SYMBOL_PATTERN = "¤";

    private final DateTimeFormatter dateTimeFormat;
    private final DateTimeFormatter dateFormat;
    private final DateTimeFormatter timeFormat;
    private final String dateTimeFormatPattern;
    private final String dateFormatPattern;
    private final String timeFormatPattern;
    private final DecimalFormat decimalFormat;
    private final DecimalFormat integerFormat;
    private final DecimalFormat currencyFormat;
    private final String cellDateTimeFormat;
    private final String cellDateFormat;
    private final String cellTimeFormat;
    private final String cellCurrencyFormat;
    private final String cellDecimalFormat;
    private final String cellIntegerFormat;
    private final Locale locale;

    public ExporterFormatter(Locale locale) {
        this.locale = locale;
        this.dateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM).withLocale(locale);
        this.dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale);
        this.timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(locale);
        this.dateTimeFormatPattern = getPatternFromDateTimeFormatter(dateTimeFormat);
        this.dateFormatPattern = getPatternFromDateTimeFormatter(dateFormat);
        this.timeFormatPattern = getPatternFromDateTimeFormatter(timeFormat);
        this.decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        this.integerFormat = (DecimalFormat) DecimalFormat.getIntegerInstance(locale);
        this.currencyFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale == Locale.ENGLISH ? Locale.US : locale);
        this.cellDateTimeFormat = DateFormatConverter.convert(locale, dateTimeFormatPattern);
        this.cellDateFormat = DateFormatConverter.convert(locale, dateFormatPattern);
        this.cellTimeFormat = DateFormatConverter.convert(locale, timeFormatPattern);
        this.cellDecimalFormat = DateFormatConverter.convert(locale, decimalFormat.toPattern());
        this.cellIntegerFormat = DateFormatConverter.convert(locale, integerFormat.toPattern());
        this.cellCurrencyFormat = DateFormatConverter.convert(locale, getCurrencyFormatString());
    }

    public ExporterFormatter(String dateTimeFormat, String dateFormat, String timeFormat, String decimalFormat, String integerFormat, String currencyFormat) {
        this(dateTimeFormat, dateFormat, timeFormat, decimalFormat, integerFormat, currencyFormat, Locale.getDefault());
    }

    public ExporterFormatter(String dateTimeFormat, String dateFormat, String timeFormat, String decimalFormat, String integerFormat, String currencyFormat, Locale locale) {
        this.locale = locale;
        this.dateTimeFormat = DateTimeFormatter.ofPattern(dateTimeFormat).withLocale(locale);
        this.dateFormat = DateTimeFormatter.ofPattern(dateFormat).withLocale(locale);
        this.timeFormat = DateTimeFormatter.ofPattern(timeFormat).withLocale(locale);
        this.dateTimeFormatPattern = dateTimeFormat;
        this.dateFormatPattern = dateFormat;
        this.timeFormatPattern = timeFormat;
        this.decimalFormat = new DecimalFormat(decimalFormat, DecimalFormatSymbols.getInstance(locale));
        this.integerFormat = new DecimalFormat(integerFormat, DecimalFormatSymbols.getInstance(locale));
        this.currencyFormat = new DecimalFormat(currencyFormat, DecimalFormatSymbols.getInstance(locale));
        this.cellDateTimeFormat = DateFormatConverter.convert(locale, dateTimeFormat);
        this.cellDateFormat = DateFormatConverter.convert(locale, dateFormat);
        this.cellTimeFormat = DateFormatConverter.convert(locale, timeFormat);
        this.cellCurrencyFormat = DateFormatConverter.convert(locale, currencyFormat);
        this.cellDecimalFormat = DateFormatConverter.convert(locale, decimalFormat);
        this.cellIntegerFormat = DateFormatConverter.convert(locale, integerFormat);
    }

    public ExporterFormatter(String dateTimeFormat, String dateFormat, String timeFormat, Locale locale) {
        this.locale = locale;
        this.dateTimeFormat = DateTimeFormatter.ofPattern(dateTimeFormat).withLocale(locale);
        this.dateFormat = DateTimeFormatter.ofPattern(dateFormat).withLocale(locale);
        this.timeFormat = DateTimeFormatter.ofPattern(timeFormat).withLocale(locale);
        this.dateTimeFormatPattern = dateTimeFormat;
        this.dateFormatPattern = dateFormat;
        this.timeFormatPattern = timeFormat;
        this.decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        this.integerFormat = (DecimalFormat) DecimalFormat.getIntegerInstance(locale);
        this.currencyFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale == Locale.ENGLISH ? Locale.US : locale);
        this.cellDateTimeFormat = DateFormatConverter.convert(locale, dateTimeFormatPattern);
        this.cellDateFormat = DateFormatConverter.convert(locale, dateFormatPattern);
        this.cellTimeFormat = DateFormatConverter.convert(locale, timeFormatPattern);
        this.cellDecimalFormat = DateFormatConverter.convert(locale, decimalFormat.toPattern());
        this.cellIntegerFormat = DateFormatConverter.convert(locale, integerFormat.toPattern());
        this.cellCurrencyFormat = DateFormatConverter.convert(locale, getCurrencyFormatString());
    }

    private String getCurrencyFormatString() {
        String cf = this.currencyFormat.toPattern();
        cf = cf.replace(CURRENCY_SYMBOL_PATTERN, this.currencyFormat.getPositivePrefix());
        return cf;
    }

    public String getLocalDateTimeFormat() {
        return dateTimeFormatPattern;
    }

    public String getLocalDateFormat() {
        return dateFormatPattern;
    }

    public String getLocalTimeFormat() {
        return timeFormatPattern;
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

    public String formatDate(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dateFormat.format(localDateTime.toLocalDate());
    }

    public String formatTime(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return timeFormat.format(localDateTime.toLocalTime());
    }

    public String formatDateTime(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dateTimeFormat.format(localDateTime);
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
        if (o instanceof Date) {
            return formatByDateType((Date) o);
        }
        if (o instanceof Number) {
            if (isCurrency) {
                return formatCurrency((Number) o);
            }
            return formatNumber((Number) o);
        }
        return o.toString();
    }

    public String formatByDateType(Date date) {
        DateType dateType = ExporterDateUtils.getDateType(date);
        if (dateType == DateType.NO_DATE) {
            return null;
        }
        if (dateType == DateType.DATE) {
            return formatDate(date);
        }
        if (dateType == DateType.TIME) {
            return formatTime(date);
        }
        return formatDateTime(date);
    }

    public String getCellStringFormatByType(Object o, boolean isCurrency) {
        if (o instanceof Date) {
            Date date = (Date) o;
            DateType dateType = ExporterDateUtils.getDateType(date);
            if (dateType == DateType.NO_DATE) {
                return null;
            }
            if (dateType == DateType.DATE) {
                return cellDateFormat;
            }
            if (dateType == DateType.TIME) {
                return cellTimeFormat;
            }
            return cellDateTimeFormat;
        }
        if (o instanceof Integer || o instanceof Long) {
            return cellIntegerFormat;
        }
        if (o instanceof Number && isCurrency) {
            return cellCurrencyFormat;
        }
        if (o instanceof Number) {
            return cellDecimalFormat;
        }
        return null;
    }

    public String getCellStringFormatByUserType(Object o, CellType cellType) {
        if (cellType == CellType.DATE_TYPE) {
            return cellDateFormat;
        }
        if (cellType == CellType.TIME_TYPE) {
            return cellTimeFormat;
        }
        if (cellType == CellType.DATETIME_TYPE) {
            return cellDateTimeFormat;
        }
        if (cellType == CellType.CURRENCY_TYPE) {
            return cellCurrencyFormat;
        }
        if (cellType == CellType.NUMERIC_TYPE) {
            if (o instanceof Integer || o instanceof Long) {
                return cellIntegerFormat;
            }
            return cellDecimalFormat;
        }
        return null;
    }

    private String getPatternFromDateTimeFormatter(DateTimeFormatter dateTimeFormat) {
        return DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, FormatStyle.MEDIUM,
                Chronology.ofLocale(dateTimeFormat.getLocale()), dateTimeFormat.getLocale());
    }

}
