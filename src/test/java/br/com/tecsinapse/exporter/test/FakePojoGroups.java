/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.annotation.TableCellMappings;
import br.com.tecsinapse.exporter.converter.group.Default;

public class FakePojoGroups {

    private String one;
    private String two;
    private String three;

    @TableCellMappings({
            @TableCellMapping(columnIndex = 0),
            @TableCellMapping(columnIndex = 1, groups = TestGroup.class)
    })
    public void setOne(String one) {
        this.one = one;
    }


    @TableCellMappings({
            @TableCellMapping(columnIndex = 1, groups = TestDefaultExtendedGroup.class),
            @TableCellMapping(columnIndex = 0, groups = TestGroup.class)
    })
    public void setTwo(String two) {
        this.two = two;
    }


    @TableCellMapping(columnIndex = 2, groups = {TestDefaultExtendedGroup.class, Default.class})
    public void setThree(String three) {
        this.three = three;
    }

    @Override
    public String toString() {
        return "FakePojoGroups{" +
                "one='" + one + '\'' +
                ", two='" + two + '\'' +
                ", three='" + three + '\'' +
                '}';
    }
}
