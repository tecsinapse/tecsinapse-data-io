/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import java.math.BigDecimal;

import com.google.common.base.Strings;

public class IntegerFromBigDecimalTableCellConverter implements FromNumberConverter<Integer> {

    @Override
    public Integer apply(BigDecimal input) {
        return input.intValue();
    }

    @Override
    public Integer apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : new BigDecimal(input).intValue();
    }

}
