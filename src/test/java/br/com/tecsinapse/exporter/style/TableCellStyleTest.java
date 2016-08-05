/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.style;

import static br.com.tecsinapse.datasources.HSSFColorDs.BLACK;
import static br.com.tecsinapse.datasources.HSSFColorDs.BLUE;
import static br.com.tecsinapse.datasources.HSSFColorDs.GREEN;
import static br.com.tecsinapse.datasources.HSSFColorDs.RED;
import static br.com.tecsinapse.datasources.HSSFColorDs.WHITE;
import static br.com.tecsinapse.exporter.style.CssStyle.BOLD;
import static br.com.tecsinapse.exporter.style.CssStyle.ITALIC;
import static br.com.tecsinapse.exporter.style.CssStyle.STRIKEOUT;
import static br.com.tecsinapse.exporter.style.CssStyle.UNDERLINE;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.AUTOMATIC;
import org.apache.poi.ss.usermodel.CellStyle;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TableCellStyleTest {

    @DataProvider(name = "cssDs")
    private Object[][] cssDataSource() {
        return new Object[][] {
                {BLACK, 11, RED, null,
                        "background-color:#000000;" + BOLD.getCss() + ITALIC.getCss() + STRIKEOUT.getCss() + UNDERLINE.getCss() + CssStyle.toFontSize(11) + CssStyle.toTextColor(RED),
                        WHITE, "background-color:#FFFFFF;font-size:11;color:#FF0000;",
                        true, true, true, true},
                {WHITE, null, null, null,
                        "background-color:#FFFFFF;" + BOLD.getCss() + ITALIC.getCss() + STRIKEOUT.getCss() + UNDERLINE.getCss(),
                        RED, "background-color:#FF0000;",
                        true, true, true, true},
                {RED, null, null, null,
                        "background-color:#FF0000;" + BOLD.getCss() + STRIKEOUT.getCss(),
                        BLUE, "background-color:#0000FF;" + ITALIC.getCss() + UNDERLINE.getCss(),
                        true, false, true, false},
                {BLUE, null, null, null,
                        "background-color:#0000FF;" + BOLD.getCss() + UNDERLINE.getCss(),
                        GREEN, "background-color:#00FF00;" + ITALIC.getCss() + STRIKEOUT.getCss(),
                        true, false, false, true},
                {GREEN, null, null, null,
                        "background-color:#00FF00;" + BOLD.getCss() + ITALIC.getCss() + STRIKEOUT.getCss() + UNDERLINE.getCss(),
                        BLACK, "background-color:#000000;",
                        true, true, true, true},
                {null, 14, BLUE, CellStyleBorder.DEFAULT,
                        CellStyleBorder.DEFAULT.toCss() + BOLD.getCss() + ITALIC.getCss() + STRIKEOUT.getCss() + UNDERLINE.getCss() + CssStyle.toFontSize(14) + CssStyle.toTextColor(BLUE),
                        BLACK, "background-color:#000000;border:solid #000000 1px;font-size:14;color:#0000FF;",
                        true, true, true, true}
        };
    }

    @DataProvider(name = "toStyleDs")
    private Object[][] toStyleDataSource() {
        return new Object[][] {
                {BLACK, BLACK.getIndex(), RED, RED.getIndex()},
                {WHITE, WHITE.getIndex(), RED, RED.getIndex()},
                {null, AUTOMATIC.index, GREEN, GREEN.getIndex()}
        };
    }

    @Test(dataProvider = "cssDs")
    public void cssStyleTest(HSSFColor hssfColor, Integer fontSize, HSSFColor fontColor, CellStyleBorder border, String cssStyle, HSSFColor hssfColorPost, String cssStylePost,
                             boolean bold, boolean italic, boolean strikeout, boolean underline) {
        TableCellStyle style = new TableCellStyle(hssfColor);
        style.setFontColor(fontColor);
        style.setFontSize(fontSize);
        style.setBorder(border);
        style.setBold(bold);
        style.setItalic(italic);
        style.setStrikeout(strikeout);
        style.setUnderline(underline);

        Assert.assertEquals(style.isBold(), bold);
        Assert.assertEquals(style.isItalic(), italic);
        Assert.assertEquals(style.isStrikeout(), strikeout);
        Assert.assertEquals(style.isUnderline(), underline);
        Assert.assertEquals(style.getCssStyle(), cssStyle);

        TableCellStyle style2 = style.clone();
        style2.setBold(!bold);
        style2.setItalic(!italic);
        style2.setStrikeout(!strikeout);
        style2.setUnderline(!underline);

        style2.setBackgroundColor(hssfColorPost);
        Assert.assertEquals(style2.isBold(), !bold);
        Assert.assertEquals(style2.isItalic(), !italic);
        Assert.assertEquals(style2.isStrikeout(), !strikeout);
        Assert.assertEquals(style2.isUnderline(), !underline);
        Assert.assertEquals(style2.getCssStyle(), cssStylePost);
    }

    @Test(dataProvider = "toStyleDs")
    public void toCellStyleTest(HSSFColor bgColor, int bgIndex, HSSFColor fontColor, int fontIndex) {
        HSSFWorkbook wb = new HSSFWorkbook();
        TableCellStyle style = new TableCellStyle(bgColor);
        style.setFontColor(fontColor);
        CellStyle cellStyle = style.toCellStyle(wb, null);
        Assert.assertEquals(cellStyle.getFillForegroundColor(), bgIndex);
    }

}