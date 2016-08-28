/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.test;

import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.util.SpreadsheetUtil;

public class SpreadsheetUtilTest {

    @DataProvider(name = "columnNames")
    public Object[][] columnNames() throws URISyntaxException {

        return new Object[][]{
                {"A1", 0},
                {"B", 1},
        };
    }

    @Test(dataProvider = "columnNames")
    public void getColumnIndex(String columnName, int result) {
        Assert.assertEquals(SpreadsheetUtil.getColumnIndexByColumnName(columnName), result);
    }

    @DataProvider(name = "columnIndexes")
    public Object[][] columnIndexes() throws URISyntaxException {

        return new Object[][]{
                {0, "A"},
                {1, "B"},
        };
    }

    @Test(dataProvider = "columnIndexes")
    public void getColumnName(int columnIndex, String result) {
        Assert.assertEquals(SpreadsheetUtil.getColumnNameByColumnIndex(columnIndex), result);
    }
}

