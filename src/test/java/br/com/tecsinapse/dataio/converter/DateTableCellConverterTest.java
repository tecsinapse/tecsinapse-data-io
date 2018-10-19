/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.converter;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.DataProvider;

public class DateTableCellConverterTest extends AbstractTableCellConverter<Date, DateTableCellConverter> {

    private static final String DATE = "2016-04-16T00:00:00";

    private final DateTableCellConverter converter = new DateTableCellConverter();

    @Override
    protected DateTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    protected Object[][] getValues() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.APRIL, 16, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {DATE, calendar.getTime()}
        };
    }

}
