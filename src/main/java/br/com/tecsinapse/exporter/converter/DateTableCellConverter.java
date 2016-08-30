/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import java.util.Date;

import com.google.common.base.Strings;

import br.com.tecsinapse.exporter.util.ExporterDateUtils;

public class DateTableCellConverter implements FromDateConverter<Date> {

    @Override
    public Date apply(Date input) {
        return input;
    }

    @Override
    public Date apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : ExporterDateUtils.parseDate(input);
    }

}
