/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CellStyleBorderDefaultTest {

    private CellStyleBorderDefault objTest;

    @BeforeClass
    public void setUpClass() {
        objTest = new CellStyleBorderDefault();
    }

    @Test(expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBorderColor() throws Exception {
        objTest.setBorderColor(null);
    }

    @Test(expectedExceptions = {UnsupportedOperationException.class})
    public void testSetBottom() throws Exception {
        objTest.setBottom(true);
    }

    @Test(expectedExceptions = {UnsupportedOperationException.class})
    public void testSetLeft() throws Exception {
        objTest.setLeft(true);
    }

    @Test(expectedExceptions = {UnsupportedOperationException.class})
    public void testSetRight() throws Exception {
        objTest.setRight(true);
    }

    @Test(expectedExceptions = {UnsupportedOperationException.class})
    public void testSetTop() throws Exception {
        objTest.setTop(true);
    }

}