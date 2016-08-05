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

import br.com.tecsinapse.exporter.util.Constants;

public class ExporterFormatterTest {

    @DataProvider(name = "exporterFormatterDs")
    private Object[][] exporterFormatterDs() {
        return new Object[][]{
                { Locale.ENGLISH, ExporterFormatter.DEFAULT, LocalDateTime.parse("2016-02-28T15:45:46"), "Feb 28, 2016 3:45:46 PM", LocalDate.parse("2016-02-28"), "Feb 28, 2016", LocalTime.parse("15:45:46"), "3:45 PM", 12025.5, "12,025.5"},
                { Constants.LOCALE_PT_BR, ExporterFormatter.PT_BR, LocalDateTime.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", LocalDate.parse("2016-02-28"), "28/02/2016", LocalTime.parse("15:45:46"), "15:45", 12025.5, "12.025,5"},
                { Locale.ENGLISH, ExporterFormatter.DEFAULT, LocalDateTime.parse("2016-02-28T15:45:46"), "Feb 28, 2016 3:45:46 PM", LocalDate.parse("2016-02-28"), "Feb 28, 2016", LocalTime.parse("15:45:46"), "3:45 PM", 12025, "12,025"},
                { Constants.LOCALE_PT_BR, ExporterFormatter.PT_BR, LocalDateTime.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", LocalDate.parse("2016-02-28"), "28/02/2016", LocalTime.parse("15:45:46"), "15:45", 12025, "12.025"},
                { Locale.ENGLISH, new ExporterFormatter("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "#,##0.00", "#,###", "R$ #,##0.00", Locale.ENGLISH), LocalDateTime.parse("2016-02-28T15:45:46"), "2016-02-28 15:45:46", LocalDate.parse("2016-02-28"), "2016-02-28", LocalTime.parse("15:45:46"), "15:45:46", 12025.5, "12,025.50"},
                { Constants.LOCALE_PT_BR, new ExporterFormatter("dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy", "HH:mm:ss", "#,##0.00", "#,###", "R$ #,##0.00", new Locale("pt", "BR")), LocalDateTime.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", LocalDate.parse("2016-02-28"), "28/02/2016", LocalTime.parse("15:45:46"), "15:45:46", 12025.5, "12.025,50"}
        };
    }

    @Test(dataProvider = "exporterFormatterDs")
    public void ExporterFormatterTest(Locale locale, ExporterFormatter formatter, LocalDateTime ldt, String ldtExpected,
                                      LocalDate ld, String ldExpected, LocalTime lt, String ltExpected,
                                      Number number, String nExpected) {

        Locale.setDefault(locale);
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