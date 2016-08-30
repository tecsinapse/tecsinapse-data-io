/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.test;

import java.math.BigDecimal;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.BigDecimalTableCellConverter;

public class FakePojoNumeric {

    private BigDecimal value1;
    private BigDecimal value2;
    private BigDecimal value3;
    private BigDecimal value4;
    private BigDecimal value5;

    @TableCellMapping(columnIndex = 0, converter = BigDecimalTableCellConverter.class)
    public void setValue1(BigDecimal value1) {
        this.value1 = value1;
    }

    @TableCellMapping(columnIndex = 1, converter = BigDecimalTableCellConverter.class)
    public void setValue2(BigDecimal value2) {
        this.value2 = value2;
    }

    @TableCellMapping(columnIndex = 2, converter = BigDecimalTableCellConverter.class)
    public void setValue3(BigDecimal value3) {
        this.value3 = value3;
    }

    @TableCellMapping(columnIndex = 3, converter = BigDecimalTableCellConverter.class)
    public void setValue4(BigDecimal value4) {
        this.value4 = value4;
    }

    @TableCellMapping(columnIndex = 4, converter = BigDecimalTableCellConverter.class)
    public void setValue5(BigDecimal value5) {
        this.value5 = value5;
    }


    @Override
    public String toString() {
        return "FakePojoNumeric{" +
                "value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                ", value3='" + value3 + '\'' +
                ", value4='" + value4 + '\'' +
                ", value5='" + value5 + '\'' +
                '}';
    }

}
