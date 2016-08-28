/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TableCellStyleDefaultNotSetsTest {

    @DataProvider(name = "objsNotSetsDs")
    public Object[][] objsNotSetsDs() {
        return new Object[][] {
                {new TableCellStyleDefaultBody()},
                {new TableCellStyleDefaultHeaderFooter()}
        };
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetFontColor(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setFontColor(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBackgroundColor(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setBackgroundColor(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetFontSize(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setFontSize(5);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBold(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setBold(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetItalic(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setItalic(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetUnderline(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setUnderline(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetStrikeout(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setStrikeout(true);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetvAlign(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setvAlign(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSethAlign(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.sethAlign(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBorder(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setBorder(null);
    }

    @Test(dataProvider = "objsNotSetsDs", expectedExceptions = {UnsupportedOperationException.class})
    public void testSetCellFormat(TableCellStyle tableCellStyle) throws Exception {
        tableCellStyle.setCellFormat(null);
    }

}