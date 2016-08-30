/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CssStyleTest {
    @Test
    public void checkNullAsEmpty() {
        Assert.assertTrue(CssStyle.toTextColor(null).isEmpty());
        Assert.assertTrue(CssStyle.toBackgroundColor(null).isEmpty());
    }
}