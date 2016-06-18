/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.test;


import java.util.Objects;

import br.com.tecsinapse.exporter.annotation.FixedLengthColumn;
import br.com.tecsinapse.exporter.converter.IntegerTableCellConverter;

public class FakeFixedLengthFilePojoUseLineLength {

    private int one;
    private String two;

    public FakeFixedLengthFilePojoUseLineLength() {
    }

    public FakeFixedLengthFilePojoUseLineLength(int one, String two) {
        this.one = one;
        this.two = two;
    }

    @FixedLengthColumn(columnIndex = 0, columnSize = 2, converter = IntegerTableCellConverter.class)
    public void setOne(int one) {
        this.one = one;
    }

    @FixedLengthColumn(columnIndex = 2, useLineLength = true)
    public void setTwo(String two) {
        this.two = two;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FakeFixedLengthFilePojoUseLineLength)) {
            return false;
        }
        FakeFixedLengthFilePojoUseLineLength other = (FakeFixedLengthFilePojoUseLineLength) obj;
        return Objects.equals(one, other.one) && Objects.equals(two, other.two);
    }

    @Override
    public String toString() {
        return "FakeFixedLengthFilePojoUseLineLength{" + "one='" + one + '\'' + ", two='" + two + '\'' + '}';
    }
}
