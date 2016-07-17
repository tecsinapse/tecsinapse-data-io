/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter;

import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExporterFormatterTest {

    @DataProvider(name = "exporterFormatterDs")
    private Object[][] exporterFormatterDs() {
        return new Object[][]{
                { ExporterFormatter.DEFAULT, LocalDateTime.parse("2016-02-28T15:45:46"), "2016-02-28 15:45:46", LocalDate.parse("2016-02-28"), "2016-02-28", LocalTime.parse("15:45:46"), "15:45", 12025.5, "12,025.5"},
                { ExporterFormatter.PT_BR, LocalDateTime.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", LocalDate.parse("2016-02-28"), "28/02/2016", LocalTime.parse("15:45:46"), "15:45", 12025.5, "12.025,5"},
                { ExporterFormatter.DEFAULT, LocalDateTime.parse("2016-02-28T15:45:46"), "2016-02-28 15:45:46", LocalDate.parse("2016-02-28"), "2016-02-28", LocalTime.parse("15:45:46"), "15:45", 12025, "12,025"},
                { ExporterFormatter.PT_BR, LocalDateTime.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", LocalDate.parse("2016-02-28"), "28/02/2016", LocalTime.parse("15:45:46"), "15:45", 12025, "12.025"},
                { new ExporterFormatter("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "#,##0.00", Locale.ENGLISH), LocalDateTime.parse("2016-02-28T15:45:46"), "2016-02-28 15:45:46", LocalDate.parse("2016-02-28"), "2016-02-28", LocalTime.parse("15:45:46"), "15:45:46", 12025.5, "12,025.50"},
                { new ExporterFormatter("dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy", "HH:mm:ss", "#,##0.00", new Locale("pt", "BR")), LocalDateTime.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", LocalDate.parse("2016-02-28"), "28/02/2016", LocalTime.parse("15:45:46"), "15:45:46", 12025.5, "12.025,50"}
        };
    }

    @Test(dataProvider = "exporterFormatterDs")
    public void ExporterFormatterTest(ExporterFormatter formatter, LocalDateTime ldt, String ldtExpected,
                                      LocalDate ld, String ldExpected, LocalTime lt, String ltExpected,
                                      Number number, String nExpected) {

        Assert.assertNotNull(formatter.getLocalDateTimeFormat());
        Assert.assertNotNull(formatter.getLocalDateFormat());
        Assert.assertNotNull(formatter.getLocalTimeFormat());
        Assert.assertNotNull(formatter.getDecimalFormater());

        Assert.assertEquals(formatter.formatLocalDateTime(ldt), ldtExpected);
        Assert.assertEquals(formatter.formatLocalDate(ld), ldExpected);
        Assert.assertEquals(formatter.formatLocalTime(lt), ltExpected);
        Assert.assertEquals(formatter.formatNumber(number), nExpected);
    }

}