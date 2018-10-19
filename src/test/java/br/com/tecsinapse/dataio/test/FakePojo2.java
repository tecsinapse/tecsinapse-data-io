/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.test;

import org.joda.time.LocalDate;

import br.com.tecsinapse.dataio.annotation.TableCellMapping;
import br.com.tecsinapse.dataio.converter.LocalDateTableCellConverter;

public class FakePojo2 {

    private LocalDate data1;
    private LocalDate data2;
    private LocalDate data3;

    @TableCellMapping(columnIndex = 0, converter = LocalDateTableCellConverter.class)
    public void setData1(LocalDate data1) {
        this.data1 = data1;
    }

    @TableCellMapping(columnIndex = 1, converter = LocalDateTableCellConverter.class)
    public void setData2(LocalDate data2) {
        this.data2 = data2;
    }

    @TableCellMapping(columnIndex = 2, converter = LocalDateTableCellConverter.class)
    public void setData3(LocalDate data3) {
        this.data3 = data3;
    }

    @Override
    public String toString() {
        return "FakePojo2{" +
                "data1='" + toString(data1) + '\'' +
                ", data2='" + toString(data2) + '\'' +
                ", data3='" + toString(data3) + '\'' +
                '}';
    }

    String toString(LocalDate data) {
        return data != null ? data.toString() : null;
    }
}
