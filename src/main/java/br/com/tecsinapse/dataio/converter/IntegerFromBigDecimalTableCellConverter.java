/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.math.BigDecimal;

import br.com.tecsinapse.dataio.util.CommonUtils;

public class IntegerFromBigDecimalTableCellConverter implements FromNumberConverter<Integer> {

    @Override
    public Integer apply(BigDecimal input) {
        return input.intValue();
    }

    @Override
    public Integer apply(String input) {
        return CommonUtils.isNullOrEmpty(input) ? null : new BigDecimal(input).intValue();
    }

}
