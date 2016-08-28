/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.util.Constants;

public class ExporterFormatterTest {

    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    @DataProvider(name = "exporterFormatterDs")
    private Object[][] exporterFormatterDs() throws ParseException {
        return new Object[][]{
                { Locale.ENGLISH, ExporterFormatter.DEFAULT, DATETIME_FORMAT.parse("2016-02-28T15:45:46"), "Feb 28, 2016 3:45:46 PM", DATE_FORMAT.parse("2016-02-28"), "Feb 28, 2016", TIME_FORMAT.parse("15:45:46"), "3:45 PM", 12025.5, "12,025.5"},
                { Constants.LOCALE_PT_BR, ExporterFormatter.PT_BR, DATETIME_FORMAT.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", DATE_FORMAT.parse("2016-02-28"), "28/02/2016", TIME_FORMAT.parse("15:45:46"), "15:45", 12025.5, "12.025,5"},
                { Locale.ENGLISH, ExporterFormatter.DEFAULT, DATETIME_FORMAT.parse("2016-02-28T15:45:46"), "Feb 28, 2016 3:45:46 PM", DATE_FORMAT.parse("2016-02-28"), "Feb 28, 2016", TIME_FORMAT.parse("15:45:46"), "3:45 PM", 12025, "12,025"},
                { Constants.LOCALE_PT_BR, ExporterFormatter.PT_BR, DATETIME_FORMAT.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", DATE_FORMAT.parse("2016-02-28"), "28/02/2016", TIME_FORMAT.parse("15:45:46"), "15:45", 12025, "12.025"},
                { Locale.ENGLISH, new ExporterFormatter("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "#,##0.00", "#,###", "R$ #,##0.00", Locale.ENGLISH), DATETIME_FORMAT.parse("2016-02-28T15:45:46"), "2016-02-28 15:45:46", DATE_FORMAT.parse("2016-02-28"), "2016-02-28", TIME_FORMAT.parse("15:45:46"), "15:45:46", 12025.5, "12,025.50"},
                { Constants.LOCALE_PT_BR, new ExporterFormatter("dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy", "HH:mm:ss", "#,##0.00", "#,###", "R$ #,##0.00", new Locale("pt", "BR")), DATETIME_FORMAT.parse("2016-02-28T15:45:46"), "28/02/2016 15:45:46", DATE_FORMAT.parse("2016-02-28"), "28/02/2016", TIME_FORMAT.parse("15:45:46"), "15:45:46", 12025.5, "12.025,50"}
        };
    }

    @Test(dataProvider = "exporterFormatterDs")
    public void ExporterFormatterTest(Locale locale, ExporterFormatter formatter, Date dt, String ldtExpected,
                                      Date d, String ldExpected, Date t, String ltExpected,
                                      Number number, String nExpected) {

        Locale.setDefault(locale);
        Assert.assertNotNull(formatter.getLocalDateTimeFormat());
        Assert.assertNotNull(formatter.getLocalDateFormat());
        Assert.assertNotNull(formatter.getLocalTimeFormat());
        Assert.assertNotNull(formatter.getDecimalFormater());

        Assert.assertEquals(formatter.formatDateTime(dt), ldtExpected);
        Assert.assertEquals(formatter.formatDate(d), ldExpected);
        Assert.assertEquals(formatter.formatTime(t), ltExpected);
        Assert.assertEquals(formatter.formatNumber(number), nExpected);
    }

}