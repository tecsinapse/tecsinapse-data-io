/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.testng.annotations.DataProvider;

public class LocalDateTimeTableCellConverterTest extends AbstractFromDataTableCellConverter<LocalDateTime, LocalDateTimeTableCellConverter> {

    private static final String LOCAL_DATE_TIME = "2016-01-01T12:45:56";
    private static final Date DATE = Date.from(LocalDateTime.parse("2016-01-01T12:45:56").atZone(ZoneId.systemDefault()).toInstant());

    private final LocalDateTimeTableCellConverter converter = new LocalDateTimeTableCellConverter();

    @Override
    protected LocalDateTimeTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    protected Object[][] getValues() {
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {LOCAL_DATE_TIME, LocalDateTime.parse(LOCAL_DATE_TIME)},
                {DATE, LocalDateTime.parse(LOCAL_DATE_TIME)}
        };
    }

}
