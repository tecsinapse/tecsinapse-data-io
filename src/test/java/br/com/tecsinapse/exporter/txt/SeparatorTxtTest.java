/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.txt;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SeparatorTxtTest {

    @DataProvider(name = "separatorDs")
    public Object[][] separatorDs() {
        return new Object[][] {
                {SeparatorTxt.TAB, "\t"},
                {SeparatorTxt.NONE, ""},
                {SeparatorTxt.SPACE, " "},
                {SeparatorTxt.SEMICOLON, ";"},
                {SeparatorTxt.COMMA, ","}
        };

    }

    @Test
    public void testSeparatorTxtSize() {
        Assert.assertEquals(SeparatorTxt.values().length, 5);
    }

    @Test(dataProvider = "separatorDs")
    public void testGetSeparator(SeparatorTxt separatorTxt, String expected) {
        Assert.assertEquals(separatorTxt.getSeparator(), expected);
    }

}