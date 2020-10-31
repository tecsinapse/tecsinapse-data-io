/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.math.BigDecimal;

import br.com.tecsinapse.dataio.util.CommonUtils;

public class BigDecimalTableCellConverter implements Converter<BigDecimal, BigDecimal> {

    @Override
    public BigDecimal apply(BigDecimal input) {
        return input;
    }

    @Override
    public BigDecimal apply(String input) {
        return CommonUtils.isNullOrEmpty(input) ? null : new BigDecimal(input);
    }

}
