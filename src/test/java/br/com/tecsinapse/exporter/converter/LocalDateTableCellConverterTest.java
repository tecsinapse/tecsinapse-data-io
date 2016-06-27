/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import org.joda.time.LocalDate;
import org.testng.annotations.DataProvider;

public class LocalDateTableCellConverterTest extends AbstractTableCellConverter<LocalDate, LocalDateTableCellConverter> {

    private static final String LOCAL_DATE = "2016-01-01";

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
                {LOCAL_DATE, LocalDate.parse(LOCAL_DATE)}
        };
    }

}
