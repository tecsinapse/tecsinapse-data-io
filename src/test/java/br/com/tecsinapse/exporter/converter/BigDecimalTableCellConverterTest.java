/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import java.math.BigDecimal;

import org.testng.annotations.DataProvider;

public class BigDecimalTableCellConverterTest extends AbstractTableCellConverter<BigDecimal, BigDecimalTableCellConverter> {

    private final BigDecimalTableCellConverter converter = new BigDecimalTableCellConverter();

    @Override
    public BigDecimalTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    public Object[][] getValues() {
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {DECIMAL_VALUE, new BigDecimal(DECIMAL_VALUE)}
        };
    }

}
