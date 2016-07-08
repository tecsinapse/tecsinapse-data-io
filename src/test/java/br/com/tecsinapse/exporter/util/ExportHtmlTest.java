/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.Table;

public class ExportHtmlTest {

    @DataProvider(name = "toHtmlDs")
    private Object[][] toHtmlDs() {
        Map<String, String> tableMap = new HashMap<>();
        tableMap.put("border", "1");
        tableMap.put("style", "background-color: #DFDFDF");
        return new Object[][] {
                {null, Arrays.asList(), ""},
                {null, Arrays.asList(Arrays.asList("c1", "c2", "c3"), Arrays.asList("c4", "c5", "c6")), "<table>\n" +
                        "<tr>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c1</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c2</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c3</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c4</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c5</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c6</td>\n" +
                        "</tr>\n" +
                        "</table>\n"},
                {tableMap, Arrays.asList(Arrays.asList("c1", "c2", "c3"), Arrays.asList("c4", "c5", "c6")),
                        "<table style=\"background-color: #DFDFDF\" border=\"1\">\n" +
                        "<tr>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c1</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c2</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c3</td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c4</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c5</td>\n" +
                        "<td style=\"vertical-align: middle;\" rowspan=\"1\" colspan=\"1\">c6</td>\n" +
                        "</tr>\n" +
                        "</table>\n"}
        };
    }

    @Test(dataProvider = "toHtmlDs")
    public void testNewInstance(Map<String, String> tableProps, List<List<String>> rows, String expected) throws Exception {
        ExportHtml exportHtml = tableProps == null ? ExportHtml.newInstance() : ExportHtml.newInstance(tableProps);
        Table table = rows.isEmpty() ? null : new Table();
        for(List<String> cols : rows) {
            table.addNewRow();
            for(String col : cols) {
                table.add(col);
            }
        }
        Assert.assertEquals(exportHtml.toHtml(table), expected);
        if (tableProps != null) {
            Assert.assertEquals(exportHtml.getTableHtmlProperties().size(), tableProps.size());
        }
    }

}