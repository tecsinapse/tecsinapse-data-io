/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

import org.testng.annotations.DataProvider;

public class YearMonthTableCellConverterTest extends AbstractFromDataTableCellConverter<YearMonth, YearMonthTableCellConverter> {

    private final YearMonthTableCellConverter converter = new YearMonthTableCellConverter();

    private static final String YEAR_MONTH_STR = "201301";
    private static final YearMonth YEAR_MONTH = YearMonth.of(2013, 1);

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
                {YEAR_MONTH_STR, YEAR_MONTH},
                {"2013-01", YEAR_MONTH},
                {"01/2013", YEAR_MONTH},
                {Date.from(YEAR_MONTH.atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), YEAR_MONTH}
        };
    }

}
