/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.dataio.Table;
import br.com.tecsinapse.dataio.TableCell;
import br.com.tecsinapse.dataio.style.Style;
import br.com.tecsinapse.dataio.style.TableCellStyle;

public class ExportHtmlTest {


    public static final String LINE_SEPARATOR = System.lineSeparator();

    @DataProvider(name = "toHtmlDs")
    private Object[][] toHtmlDs() {
        Map<String, String> tableMap = new LinkedHashMap<>();
        tableMap.put("border", "1");
        tableMap.put("style", "background-color: #DFDFDF");
        return new Object[][] {
                {null, 1, 1, Arrays.asList(Arrays.asList("c1", "c2", "c3"), Arrays.asList("c4", "c5", "c6")), false, "<table>" + LINE_SEPARATOR +
                        "<tr>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c1</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c2</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c3</td>" + LINE_SEPARATOR +
                        "</tr>" + LINE_SEPARATOR +
                        "<tr>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c4</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c5</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c6</td>" + LINE_SEPARATOR +
                        "</tr>" + LINE_SEPARATOR +
                        "</table>" + LINE_SEPARATOR},
                {tableMap, 1, 1, Arrays.asList(Arrays.asList("c1", "c2", "c3"), Arrays.asList("c4", "c5", "c6")), true,
                        "<table border=\"1\" style=\"background-color: #DFDFDF\">" + LINE_SEPARATOR +
                        "<tr>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c1</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c2</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c3</td>" + LINE_SEPARATOR +
                        "</tr>" + LINE_SEPARATOR +
                        "<tr>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c4</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c5</td>" + LINE_SEPARATOR +
                        "<td class=\"tbcs-body\" style=\"border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c6</td>" + LINE_SEPARATOR +
                        "</tr>" + LINE_SEPARATOR +
                        "</table>" + LINE_SEPARATOR},
                {tableMap, 2, 2, Arrays.asList(Arrays.asList("c1", "c2", "c3"), Arrays.asList("c4", "c5", "c6")), false,
                        "<table border=\"1\" style=\"background-color: #DFDFDF\">" + LINE_SEPARATOR +
                                "<tr>" + LINE_SEPARATOR +
                                "<td class=\"tbcs-body\" rowspan=\"2\" colspan=\"2\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c1</td>" + LINE_SEPARATOR +
                                "<td class=\"tbcs-body\" rowspan=\"2\" colspan=\"2\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c2</td>" + LINE_SEPARATOR +
                                "<td class=\"tbcs-body\" rowspan=\"2\" colspan=\"2\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c3</td>" + LINE_SEPARATOR +
                                "</tr>" + LINE_SEPARATOR +
                                "<tr>" + LINE_SEPARATOR +
                                "<td class=\"tbcs-body\" rowspan=\"2\" colspan=\"2\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c4</td>" + LINE_SEPARATOR +
                                "<td class=\"tbcs-body\" rowspan=\"2\" colspan=\"2\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c5</td>" + LINE_SEPARATOR +
                                "<td class=\"tbcs-body\" rowspan=\"2\" colspan=\"2\" style=\"background-color:#FFFFFF;border:solid #000000 1px;vertical-align:middle;text-align:left;font-size:10;\">c6</td>" + LINE_SEPARATOR +
                                "</tr>" + LINE_SEPARATOR +
                                "</table>" + LINE_SEPARATOR}
        };
    }

    @Test(dataProvider = "toHtmlDs")
    public void testNewInstance(Map<String, String> tableProps, int colspan, int rowspan,
                                List<List<String>> rows, boolean whiteAsTransparent, String expected) throws Exception {
        ExportHtml exportHtml = tableProps == null ? ExportHtml.newInstance(Charset.forName("UTF-8")) : ExportHtml.newInstance(tableProps, Charset.forName("UTF-8"));
        Table table = rows.isEmpty() ? null : new Table();
        TableCellStyle cellStyle = Style.TABLE_CELL_STYLE_BODY.duplicate();
        cellStyle.setCssWhiteAsTransparent(whiteAsTransparent);
        cellStyle.setIgnoreCssStyle(false);
        table.setTableCellStyleDefaultBody(cellStyle);
        for(List<String> cols : rows) {
            table.addNewRow();
            for(String col : cols) {
                TableCell cell = new TableCell(col)
                        .withContent(col)
                        .withColspan(colspan)
                        .withRowspan(rowspan)
                        .withTableCellStyle(cellStyle);
                table.add(cell);
            }
        }
        Assert.assertEquals(exportHtml.toHtml(table), expected);
        if (tableProps != null) {
            Assert.assertEquals(exportHtml.getTableHtmlProperties().size(), tableProps.size());
        }
    }

    @Test(expectedExceptions = {IOException.class})
    public void throwFileNotFound() throws IOException, NullPointerException {
        ExportHtml exportHtml = ExportHtml.newInstance(Charset.defaultCharset());
        exportHtml.toHtml(new Table(), new File(""));
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void throwNpe() throws IOException, NullPointerException {
        ExportHtml exportHtml = ExportHtml.newInstance(Charset.defaultCharset());
        exportHtml.toHtml(null, new File(""));
    }

}