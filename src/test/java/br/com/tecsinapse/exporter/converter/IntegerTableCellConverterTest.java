/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import org.testng.annotations.DataProvider;

public class IntegerTableCellConverterTest extends AbstractTableCellConverter<Integer, IntegerTableCellConverter> {

    private final IntegerTableCellConverter converter = new IntegerTableCellConverter();

    @Override
    protected IntegerTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    protected Object[][] getValues() {
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {"10", 10}
        };
    }

}
