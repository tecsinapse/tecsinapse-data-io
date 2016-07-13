/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import org.joda.time.LocalTime;

import com.google.common.base.Strings;

public class LocalTimeTableCellConverter implements TableCellConverter<LocalTime> {

    @Override
    public LocalTime apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : LocalTime.parse(input);
    }

}
