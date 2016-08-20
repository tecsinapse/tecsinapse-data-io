/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public abstract class AbstractTableCellConverter<T, C extends Converter<?, T>> {

    static final String EMPTY_STRING = "";
    static final String DECIMAL_VALUE = "257767336.0";

    protected abstract C getConverter();

    protected abstract Object[][] getValues();

    @Test(dataProvider = "values")
    public void testApply(final String value, final T expected) {
        final T result = this.getConverter().apply(value);
        assertEquals(result, expected);
    }

}
