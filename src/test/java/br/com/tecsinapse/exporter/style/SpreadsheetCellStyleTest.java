/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.style;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.AUTOMATIC;
import org.apache.poi.hssf.util.HSSFColor.BLACK;
import org.apache.poi.hssf.util.HSSFColor.WHITE;
import org.apache.poi.ss.usermodel.CellStyle;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SpreadsheetCellStyleTest {

    @DataProvider(name = "cssDs")
    private Object[][] cssDataSource() {
        return new Object[][] {
                {new HSSFColor.BLACK(), "background-color: #000000", new HSSFColor.WHITE(), "background-color: #FFFFFF"},
                {new HSSFColor.WHITE(), "background-color: #FFFFFF", new HSSFColor.RED(), "background-color: #FF0000"},
                {new HSSFColor.RED(), "background-color: #FF0000", new HSSFColor.BLUE(), "background-color: #0000FF"},
                {new HSSFColor.BLUE(), "background-color: #0000FF", new HSSFColor.BRIGHT_GREEN(), "background-color: #00FF00"},
                {new HSSFColor.BRIGHT_GREEN(), "background-color: #00FF00", new HSSFColor.BLACK(), "background-color: #000000"},
                {null, null, new HSSFColor.BLACK(), "background-color: #000000"}
        };
    }

    @DataProvider(name = "toStyleDs")
    private Object[][] toStyleDataSource() {
        return new Object[][] {
                {new HSSFColor.BLACK(), BLACK.index},
                {new HSSFColor.WHITE(), WHITE.index},
                {null, AUTOMATIC.index}
        };
    }

    @Test(dataProvider = "cssDs")
    public void cssStyleTest(HSSFColor hssfColor, String cssStyle, HSSFColor hssfColorPost, String cssStylePost) {
        SpreadsheetCellStyle style = new SpreadsheetCellStyle(hssfColor);
        Assert.assertEquals(style.getCssStyle(), cssStyle);
        style.setBackgroundColor(hssfColorPost);
        Assert.assertEquals(style.getCssStyle(), cssStylePost);
    }

    @Test(dataProvider = "toStyleDs")
    public void toCellStyleTest(HSSFColor color, int index) {
        SpreadsheetCellStyle style = new SpreadsheetCellStyle(color);
        HSSFWorkbook wb = new HSSFWorkbook();
        CellStyle cellStyle = style.toCellStyle(wb.createCellStyle());
        Assert.assertEquals(cellStyle.getFillForegroundColor(), index);
    }

}