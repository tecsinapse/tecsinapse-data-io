/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.google.common.base.Strings;

public class LocalDateTimeTableCellConverter implements FromDateConverter<LocalDateTime> {

    public static final LocalDateTimeTableCellConverter LOCAL_DATE_TIME_CONVERTER = new LocalDateTimeTableCellConverter();

    @Override
    public LocalDateTime apply(Date input) {
        return LocalDateTime.ofInstant(input.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public LocalDateTime apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : LocalDateTime.parse(input);
    }

}
