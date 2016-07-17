/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.ResourceUtils;
import br.com.tecsinapse.exporter.importer.parser.SpreadsheetParser;

public class ExcelParserTest {

    @DataProvider(name = "excelParserDs")
    private Object[][] excelParserDs() {
        CustomHashMap map = new CustomHashMap();
        map.add("Sheet1",
                Arrays.asList(
                        new DataParser(LocalDate.parse("2016-01-01"), LocalDateTime.parse("2016-01-01T00:13:15"), "0.5", 127, "Line 1", "", LocalTime.parse("0:47")),
                        new DataParser(LocalDate.parse("2016-02-01"), LocalDateTime.parse("2016-02-01T01:13:15"), "0.568", 135, "Line 2", "", LocalTime.parse("1:35")),
                        new DataParser(LocalDate.parse("2016-03-01"), LocalDateTime.parse("2016-03-01T02:13:15"), "0.478", 143, "Line 3", "", LocalTime.parse("2:23")),
                        new DataParser(LocalDate.parse("2016-04-01"), LocalDateTime.parse("2016-04-01T03:13:15"), "0.165", 151, "Line 4", "", LocalTime.parse("3:11")),
                        new DataParser(LocalDate.parse("2016-05-01"), LocalDateTime.parse("2016-05-01T04:13:15"), "156.23", 159, "Line 5", "", LocalTime.parse("3:59")),
                        new DataParser(LocalDate.parse("2016-06-01"), LocalDateTime.parse("2016-06-01T05:13:15"), "23", 167, "Line 6", "", LocalTime.parse("4:47")),
                        new DataParser(LocalDate.parse("2016-07-01"), LocalDateTime.parse("2016-07-01T06:13:15"), "25.68", 175, "Line 7", "", LocalTime.parse("5:35")),
                        new DataParser(LocalDate.parse("2016-08-01"), LocalDateTime.parse("2016-08-01T07:13:15"), "165.8", 183, "Line 8", "", LocalTime.parse("6:23")),
                        new DataParser(LocalDate.parse("2016-09-01"), LocalDateTime.parse("2016-09-01T08:13:15"), "145.23", 191, "Line 9", "", LocalTime.parse("7:11")),
                        new DataParser(LocalDate.parse("2016-10-01"), LocalDateTime.parse("2016-10-01T09:13:15"), "56.36", 199, "Line 10", "", LocalTime.parse("7:59")),
                        new DataParser(LocalDate.parse("2016-11-01"), LocalDateTime.parse("2016-11-01T10:13:15"), "1542.2", 207, "Line 11", "", LocalTime.parse("8:47")),
                        new DataParser(LocalDate.parse("2016-12-01"), LocalDateTime.parse("2016-12-01T11:13:15"), "5682.8", 215, "Line 12", "", LocalTime.parse("9:35")),
                        new DataParser(LocalDate.parse("2016-01-15"), LocalDateTime.parse("2017-01-15T12:13:15"), "2356.32", 223, "Line 13", "", LocalTime.parse("10:23")),
                        new DataParser(LocalDate.parse("2016-02-15"), LocalDateTime.parse("2017-02-15T13:13:15"), "46854.26", 231, "Line 14", "", LocalTime.parse("11:11")),
                        new DataParser(LocalDate.parse("2016-03-15"), LocalDateTime.parse("2017-03-15T14:13:15"), "987.35", 239, "Line 15", "", LocalTime.parse("11:59")),
                        new DataParser(LocalDate.parse("2016-04-15"), LocalDateTime.parse("2017-04-15T15:13:15"), "0.23", 247, "Line 16", "", LocalTime.parse("12:47")),
                        new DataParser(LocalDate.parse("2016-05-15"), LocalDateTime.parse("2017-05-15T16:13:15"), "0.478", 255, "Line 17", "", LocalTime.parse("13:35")),
                        new DataParser(LocalDate.parse("2016-06-15"), LocalDateTime.parse("2017-06-15T17:13:15"), "0.698", 263, "Line 18", "", LocalTime.parse("14:23")),
                        new DataParser(LocalDate.parse("2016-07-15"), LocalDateTime.parse("2017-07-15T18:13:15"), "325.6", 271, "Line 19", "", LocalTime.parse("15:11")),
                        new DataParser(LocalDate.parse("2016-08-15"), LocalDateTime.parse("2017-08-15T19:13:15"), "0.1346", 279, "Line 20", "", LocalTime.parse("15:59")),
                        new DataParser(LocalDate.parse("2016-09-15"), LocalDateTime.parse("2017-09-15T20:13:15"), "0.2653", 287, "Line 21", "", LocalTime.parse("16:47")),
                        new DataParser(LocalDate.parse("2016-10-15"), LocalDateTime.parse("2017-10-15T21:13:15"), "356", 295, "Line 22", "", LocalTime.parse("17:35")),
                        new DataParser(LocalDate.parse("2016-11-15"), LocalDateTime.parse("2017-11-15T22:13:15"), "0.3256", 303, "Line 23", "", LocalTime.parse("18:23")),
                        new DataParser(LocalDate.parse("2016-12-15"), LocalDateTime.parse("2017-12-15T23:13:15"), "0.2356", 311, "Line 24", "", LocalTime.parse("19:11"))
                )
        ).add("Sheet2",
                Arrays.asList(
                        new DataParser(LocalDate.parse("2016-03-01"), LocalDateTime.parse("2016-03-01T02:13:15"), "0.478", 143, "Line 1", "", LocalTime.parse("2:23")),
                        new DataParser(LocalDate.parse("2016-06-01"), LocalDateTime.parse("2016-06-01T05:13:15"), "23", 167, "Line 2", "", LocalTime.parse("4:47")),
                        new DataParser(LocalDate.parse("2016-09-01"), LocalDateTime.parse("2016-09-01T08:13:15"), "145.23", 191, "Line 3", "", LocalTime.parse("7:11")),
                        new DataParser(LocalDate.parse("2016-12-01"), LocalDateTime.parse("2016-12-01T11:13:15"), "5682.8", 215, "Line 4", "", LocalTime.parse("9:35")),
                        new DataParser(LocalDate.parse("2016-01-15"), LocalDateTime.parse("2017-01-15T12:13:15"), "2356.32", 223, "Line 5", "", LocalTime.parse("10:23")),
                        new DataParser(LocalDate.parse("2016-04-15"), LocalDateTime.parse("2017-04-15T15:13:15"), "0.23", 247, "Line 6", "", LocalTime.parse("12:47")),
                        new DataParser(LocalDate.parse("2016-07-15"), LocalDateTime.parse("2017-07-15T18:13:15"), "325.6", 271, "Line 7", "", LocalTime.parse("15:11")),
                        new DataParser(LocalDate.parse("2016-10-15"), LocalDateTime.parse("2017-10-15T21:13:15"), "356", 295, "Line 8", "", LocalTime.parse("17:35"))
                ));
        return new Object[][]{
                {"/files/excel-with-empty-lines.xlsx", map}
                //, {"/files/excel-with-empty-lines.xls", map}

        };
    }

    @Test(dataProvider = "excelParserDs", enabled = true)
    public void excelParserTest(String excel, CustomHashMap mapData) throws Exception {

        File file = ResourceUtils.getFileResource(excel);
        ExcelParser<DataParser> parser = new ExcelParser<DataParser>(DataParser.class, file);

        int sheet = -1;
//        Assert.assertEquals(parser.getNumberOfSheets(), mapData.size());
        for (Entry<String, List<DataParser>> entry : mapData.entrySet()) {
            String testId = String.format("[%s - %s]", excel, entry.getKey());
            sheet++;
            parser.setSheetNumber(sheet);
            Assert.assertEquals(parser.getSheetNumber(), sheet);
            List<DataParser> expected = entry.getValue();
            List<DataParser> actual = parser.parse();
            assertDataParser(actual, expected, testId);
        }

    }

    @Test(dataProvider = "excelParserDs")
    public void excelParserXlsTest(String excel, CustomHashMap mapData) throws Exception {

        File file = ResourceUtils.getFileResource(excel);
        SpreadsheetParser<DataParser> parser = new SpreadsheetParser<>(DataParser.class, file);

        int sheet = -1;
        Assert.assertEquals(parser.getNumberOfSheets(), mapData.size());
        for (Entry<String, List<DataParser>> entry : mapData.entrySet()) {
            String testId = String.format("[%s - %s]", excel, entry.getKey());
            sheet++;
            parser.setSheetNumber(sheet);
            Assert.assertEquals(parser.getSheetNumber(), sheet);
            List<DataParser> expected = entry.getValue();
            List<DataParser> actual = parser.parse();
            assertDataParser(actual, expected, testId);
        }

        List<List<String>> rows = parser.getLines();
        for (List<String> cells : rows) {

        }

    }

    private void assertDataParser(List<DataParser> actualList, List<DataParser> expectedList, String testId) {
        Assert.assertEquals(actualList.size(), expectedList.size(), testId);
        for (int i = 0; i < actualList.size(); i++) {
            DataParser actual = actualList.get(i);
            DataParser expected = expectedList.get(i);
            Assert.assertEquals(actual.getDate(), expected.getDate(), testId);
            Assert.assertEquals(actual.getDateTime(), expected.getDateTime(), testId);
            Assert.assertEquals(actual.getDecimal(), expected.getDecimal(), testId);
            Assert.assertEquals(actual.getInteger(), expected.getInteger(), testId);
            Assert.assertEquals(actual.getString(), expected.getString(), testId);
            Assert.assertEquals(actual.getEmpty(), expected.getEmpty(), testId);
            Assert.assertEquals(actual.getTime(), expected.getTime(), testId);
        }
    }

    private class CustomHashMap extends HashMap<String, List<DataParser>> {

        public CustomHashMap add(String sheet, List<DataParser> rows) {
            put(sheet, rows);
            return this;
        }

    }

}