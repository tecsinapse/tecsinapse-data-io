/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class ParserFormatter {

    public static final ParserFormatter PT_BR = new ParserFormatter("dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy", "HH:mm", "#,###,###.##########", new Locale("pt", "BR"));
    public static final ParserFormatter DEFAULT = new ParserFormatter("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm", "#,###,###.##########", Locale.ENGLISH);

    private final Locale locale;
    private final String localDateTimeFormat;
    private final String localDateFormat;
    private final String localTimeFormat;
    private final DecimalFormat decimalFormat;

    public ParserFormatter(String localDateTimeFormat, String localDateFormat, String localTimeFormat, String decimalFormat) {
        this(localDateTimeFormat, localDateFormat, localTimeFormat, decimalFormat, Locale.getDefault());
    }

    public ParserFormatter(String localDateTimeFormat, String localDateFormat, String localTimeFormat, String decimalFormat, Locale locale) {
        this.locale = locale;
        this.localDateTimeFormat = localDateTimeFormat;
        this.localDateFormat = localDateFormat;
        this.localTimeFormat = localTimeFormat;
        this.decimalFormat = new DecimalFormat(decimalFormat, DecimalFormatSymbols.getInstance(locale));
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
        return decimalFormat.format(number);
    }
}
