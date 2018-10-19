/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.time.LocalDate;
import java.util.Date;

import com.google.common.base.Strings;

public class LocalDateTableCellConverter implements FromDateConverter<LocalDate> {

    @Override
    public LocalDate apply(Date input) {
        return LocalDateTimeTableCellConverter.LOCAL_DATE_TIME_CONVERTER.apply(input).toLocalDate();
    }

    @Override
    public LocalDate apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : LocalDate.parse(input);
    }

}
