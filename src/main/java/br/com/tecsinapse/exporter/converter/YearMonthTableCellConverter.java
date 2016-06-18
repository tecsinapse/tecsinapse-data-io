/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Strings;

public class YearMonthTableCellConverter implements TableCellConverter<YearMonth> {

    private static final DateTimeFormatter YYYY_MM = DateTimeFormat.forPattern("yyyyMM");

    @Override
    public YearMonth apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : YearMonth.parse(input, YYYY_MM);
    }

}
