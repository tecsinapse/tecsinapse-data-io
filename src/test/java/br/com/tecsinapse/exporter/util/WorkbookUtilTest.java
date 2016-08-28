/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.util;

import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.Table;

public class WorkbookUtilTest {

    @Test
    public void workbookUtilTest() {
        Table table = new Table();
        LocalDateTime dateTime = LocalDateTime.parse("2016-08-01T00:00:00");
        for(int i = 0; i < 1000; i++) {
            table.addNewRow();
            table.add(i);
            for(int y = 0; y < 9; y++) {
                table.add(dateTime.plusSeconds(i+y));
            }
        }
        table.toHSSFWorkBook();
    }

}