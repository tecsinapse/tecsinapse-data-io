/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.converter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.testng.annotations.DataProvider;

public class LocalTimeTableCellConverterTest extends AbstractFromDataTableCellConverter<LocalTime, LocalTimeTableCellConverter> {

    private static final String LOCAL_TIME = "12:01:53";
    private static final Date DATE = Date.from(LocalDateTime.parse("2016-02-15T12:01:53").atZone(ZoneId.systemDefault()).toInstant());

    private final LocalTimeTableCellConverter converter = new LocalTimeTableCellConverter();

    @Override
    protected LocalTimeTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    protected Object[][] getValues() {
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {LOCAL_TIME, LocalTime.parse(LOCAL_TIME)},
                {DATE, LocalTime.parse(LOCAL_TIME)}
        };
    }

}
