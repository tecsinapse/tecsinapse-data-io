/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import java.util.Date;

import org.joda.time.YearMonth;
import org.testng.annotations.DataProvider;

public class YearMonthTableCellConverterTest extends AbstractTableCellConverter<YearMonth, YearMonthTableCellConverter> {

    private final YearMonthTableCellConverter converter = new YearMonthTableCellConverter();

    @Override
    protected YearMonthTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    protected Object[][] getValues() {
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {"201301", YearMonth.fromDateFields(new Date(1357005600000L))}
        };
    }

}
