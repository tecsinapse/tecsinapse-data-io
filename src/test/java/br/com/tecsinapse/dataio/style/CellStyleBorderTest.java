/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import static br.com.tecsinapse.dataio.style.Colors.BLACK;
import static br.com.tecsinapse.dataio.style.Colors.RED;
import static br.com.tecsinapse.dataio.style.Colors.WHITE;

import org.apache.poi.hssf.util.HSSFColor;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CellStyleBorderTest {

    @DataProvider(name = "cellStyleBorderDs")
    private Object[][] cellStyleBorderDs() {
        return new Object[][]{
                {new CellStyleBorder(BLACK, true, true, true, true), BLACK, true, true, true, true, null, "border:solid #000000 1px;", ""},
                {new CellStyleBorder(BLACK, true, false, true, false), BLACK, true, false, true, false, WHITE, "border-left:solid #000000 1px;border-top:solid #000000 1px;", "border-right:solid #FFFFFF 1px;border-bottom:solid #FFFFFF 1px;"},
                {new CellStyleBorder(RED, false, false, false, true), RED, false, false, false, true, WHITE, "border-bottom:solid #FF0000 1px;", "border-left:solid #FFFFFF 1px;border-right:solid #FFFFFF 1px;border-top:solid #FFFFFF 1px;"}
        };
    }

    @Test(dataProvider = "cellStyleBorderDs")
    public void cellStyleBorderTest(CellStyleBorder cellStyleBorder, HSSFColor color, boolean left, boolean right, boolean top, boolean bottom,
                                    HSSFColor color2, String expectedCss, String expectedInvertedCss) {
        Assert.assertEquals(cellStyleBorder.getBorderColor(), color);
        Assert.assertEquals(cellStyleBorder.isLeft(), left);
        Assert.assertEquals(cellStyleBorder.isRight(), right);
        Assert.assertEquals(cellStyleBorder.isTop(), top);
        Assert.assertEquals(cellStyleBorder.isBottom(), bottom);
        Assert.assertEquals(cellStyleBorder.toCss(), expectedCss);

        CellStyleBorder cellStyleBorder2 = cellStyleBorder.duplicate();
        cellStyleBorder2.setBorderColor(color2);
        cellStyleBorder2.setLeft(!left);
        cellStyleBorder2.setRight(!right);
        cellStyleBorder2.setTop(!top);
        cellStyleBorder2.setBottom(!bottom);

        Assert.assertEquals(cellStyleBorder2.getBorderColor(), color2);
        Assert.assertEquals(cellStyleBorder2.isLeft(), !left);
        Assert.assertEquals(cellStyleBorder2.isRight(), !right);
        Assert.assertEquals(cellStyleBorder2.isTop(), !top);
        Assert.assertEquals(cellStyleBorder2.isBottom(), !bottom);
        Assert.assertEquals(cellStyleBorder2.toCss(), expectedInvertedCss);
    }

    @Test
    public void cellStyleBorderWithSpreadsheetStyleNullTest() {
        Assert.assertNull(Style.CELL_STYLE_BORDER_DEFAULT.toCellStyle(null));
    }


}