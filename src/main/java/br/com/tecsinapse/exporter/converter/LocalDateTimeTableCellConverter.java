/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import org.joda.time.LocalDateTime;

import com.google.common.base.Strings;

public class LocalDateTimeTableCellConverter implements FromDateConverter<LocalDateTime> {

    @Override
    public LocalDateTime apply(LocalDateTime input) {
        return input;
    }

    @Override
    public LocalDateTime apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : LocalDateTime.parse(input);
    }

}
