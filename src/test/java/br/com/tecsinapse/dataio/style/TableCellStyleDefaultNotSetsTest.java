/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TableCellStyleDefaultNotSetsTest {

    @DataProvider(name = "objsNotSetsDs")
    public Object[][] objsNotSetsDs() {
        return new Object[][] {
                {new TableCellStyleDefaultBody(false, false)},
                {new TableCellStyleDefaultHeader(false)},
                {new TableCellStyleProtected(Colors.WHITE, null, true, CellHAlign.CENTER, CellVAlign.CENTER, false)}
        };
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetFontColor(TableCellStyle tableCellStyle) {
        tableCellStyle.setFontColor(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBackgroundColor(TableCellStyle tableCellStyle) {
        tableCellStyle.setBackgroundColor(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetFontSize(TableCellStyle tableCellStyle) {
        tableCellStyle.setFontSize(5);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBold(TableCellStyle tableCellStyle) {
        tableCellStyle.setBold(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetItalic(TableCellStyle tableCellStyle) {
        tableCellStyle.setItalic(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetUnderline(TableCellStyle tableCellStyle) {
        tableCellStyle.setUnderline(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetStrikeout(TableCellStyle tableCellStyle) {
        tableCellStyle.setStrikeout(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetvAlign(TableCellStyle tableCellStyle) {
        tableCellStyle.setVAlign(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSethAlign(TableCellStyle tableCellStyle) {
        tableCellStyle.setHAlign(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBorder(TableCellStyle tableCellStyle) {
        tableCellStyle.setBorder(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetCellFormat(TableCellStyle tableCellStyle) {
        tableCellStyle.setCellFormat(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetCssWhiteAsTransparent(TableCellStyle tableCellStyle) {
        tableCellStyle.setCssWhiteAsTransparent(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetCssClass(TableCellStyle tableCellStyle) {
        tableCellStyle.setCssClass(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetIgnoreCssStyle(TableCellStyle tableCellStyle) {
        tableCellStyle.setIgnoreCssStyle(true);
    }

}