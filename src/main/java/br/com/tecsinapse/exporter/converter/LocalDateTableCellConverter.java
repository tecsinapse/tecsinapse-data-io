/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class LocalDateTableCellConverter implements TableCellConverter<LocalDate> {

    private static final LocalDateTimeTableCellConverter converter = new LocalDateTimeTableCellConverter();

    @Override
    public LocalDate apply(String input) {
        LocalDateTime value = converter.apply(input);
        return value != null ? value.toLocalDate() : null;
    }

}
