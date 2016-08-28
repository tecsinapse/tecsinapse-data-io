/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.builds;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.style.TableCellStyle;

public class TableCellBuilderTest {

    @Test
    public void tableCellBuilderTest() {
        TableCell tableCell = TableCellBuilder.newTableCellBuilder()
                .tableCellStyle(TableCellStyle.BODY)
                .colspan(10)
                .rowspan(5)
                .style("noStyle")
                .content("c1")
                .build();
        Assert.assertEquals(tableCell.getTableCellStyle(), TableCellStyle.BODY);
        Assert.assertEquals(tableCell.getColspan(), Integer.valueOf(10));
        Assert.assertEquals(tableCell.getRowspan(), Integer.valueOf(5));
        Assert.assertEquals(tableCell.getStyle(), "noStyle");
        Assert.assertEquals(tableCell.getContent(), "c1");
    }

}