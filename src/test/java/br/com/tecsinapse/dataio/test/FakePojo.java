/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.test;

import br.com.tecsinapse.dataio.annotation.TableCellMapping;

public class FakePojo {

    private String one;
    private String two;
    private String three;

    @TableCellMapping(columnIndex = 0)
    public void setOne(String one) {
        this.one = one;
    }

    @TableCellMapping(columnIndex = 1)
    public void setTwo(String two) {
        this.two = two;
    }

    @TableCellMapping(columnIndex = 2)
    public void setThree(String three) {
        this.three = three;
    }

    @Override
    public String toString() {
        return "FakePojo{" +
                "one='" + one + '\'' +
                ", two='" + two + '\'' +
                ", three='" + three + '\'' +
                '}';
    }
}
