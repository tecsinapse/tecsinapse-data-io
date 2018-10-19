/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.testng.annotations.DataProvider;

public class LocalDateTableCellConverterTest extends AbstractFromDataTableCellConverter<LocalDate, LocalDateTableCellConverter> {

    private static final String LOCAL_DATE_STR = "2016-01-01";
    private static final LocalDate LOCAL_DATE = LocalDate.parse(LOCAL_DATE_STR);

    private final LocalDateTableCellConverter converter = new LocalDateTableCellConverter();

    @Override
    protected LocalDateTableCellConverter getConverter() {
        return converter;
    }

    @Override
    @DataProvider(name = "values")
    protected Object[][] getValues() {
        return new Object[][]{
                {null, null},
                {EMPTY_STRING, null},
                {LOCAL_DATE_STR, LOCAL_DATE},
                {Date.from(LOCAL_DATE.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), LOCAL_DATE}
        };
    }

}
