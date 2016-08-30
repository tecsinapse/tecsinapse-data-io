/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import java.math.BigDecimal;

import com.google.common.base.Strings;

public class LongFromDecimalTableCellConverter implements FromNumberConverter<Long> {

    @Override
    public Long apply(BigDecimal input) {
        return input.longValue();
    }

    @Override
    public Long apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : new BigDecimal(input).longValue();
    }

}
