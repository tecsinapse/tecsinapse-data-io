/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YearMonthTableCellConverter implements FromDateConverter<YearMonth> {
    private static final DateTimeFormatter YYYY_MM = DateTimeFormatter.ofPattern("yyyyMM");
    private static final Pattern DEFAULT_YM_PATTERN = Pattern.compile("\\d{4}-\\d{2}");
    private static final Pattern VALID_PATTERN = Pattern.compile("(\\d{4}.\\d{2})|(\\d{2}.\\d{4})");

    @Override
    public YearMonth apply(Date input) {
        return YearMonth.from(LocalDateTimeTableCellConverter.LOCAL_DATE_TIME_CONVERTER.apply(input));
    }

    @Override
    public YearMonth apply(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return null;
        }
        try {
            if (DEFAULT_YM_PATTERN.matcher(input).matches()) {
                return YearMonth.parse(input);
            } else {
                return YearMonth.parse(input, YYYY_MM);
            }
        } catch (DateTimeParseException e) {
            return tryParserBySplitLocalizedDateSeparator(input);
        }
    }

    private YearMonth tryParserBySplitLocalizedDateSeparator(String input) {
        if (!VALID_PATTERN.matcher(input).matches()) {
            return null;
        }
        try {
            input = input.replaceAll("[^\\d]","-");
            if (DEFAULT_YM_PATTERN.matcher(input).matches()) {
                return YearMonth.parse(input);
            }
            String[] dateInput = input.split("-");
            return YearMonth.parse(dateInput[1] + "-" + dateInput[0]);
        } catch (DateTimeParseException e) {
            log.warn("Convert YearMonth error", e);
            return null;
        }
    }

}
