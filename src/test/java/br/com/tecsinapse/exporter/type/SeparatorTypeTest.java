/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.type;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SeparatorTypeTest {

    @DataProvider(name = "separatorDs")
    public Object[][] separatorDs() {
        return new Object[][] {
                {SeparatorType.TAB, "\t"},
                {SeparatorType.NONE, ""},
                {SeparatorType.SPACE, " "},
                {SeparatorType.SEMICOLON, ";"},
                {SeparatorType.COMMA, ","}
        };

    }

    @Test
    public void testSeparatorTxtSize() {
        Assert.assertEquals(SeparatorType.values().length, 5);
    }

    @Test(dataProvider = "separatorDs")
    public void testGetSeparator(SeparatorType separatorTxt, String expected) {
        Assert.assertEquals(separatorTxt.getSeparator(), expected);
    }

}