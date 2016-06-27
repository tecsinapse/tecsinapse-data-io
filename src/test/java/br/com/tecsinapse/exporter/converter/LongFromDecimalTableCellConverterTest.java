/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import org.testng.annotations.DataProvider;

public class LongFromDecimalTableCellConverterTest extends AbstractTableCellConverter<Long, LongFromDecimalTableCellConverter> {

    private final LongFromDecimalTableCellConverter converter = new LongFromDecimalTableCellConverter();

    @Override
    protected LongFromDecimalTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    protected Object[][] getValues() {
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {DECIMAL_VALUE, 257767336L}
        };
    }

}
