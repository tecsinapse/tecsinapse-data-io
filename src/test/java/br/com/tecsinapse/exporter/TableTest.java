/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.testng.annotations.Test;

import com.google.common.io.Files;

import br.com.tecsinapse.exporter.style.Colors;
import br.com.tecsinapse.exporter.style.TableCellStyle;
import br.com.tecsinapse.exporter.util.ExporterUtil;

public class TableTest {

    @Test
    public void tableFluentRowAndCellTest() throws IOException {
        Table table = new Table();
        TableCell tableCellDate = new TableCell(2.56);
        TableCellStyle tableCellStyle = new TableCellStyle(Colors.DARK_GREEN);
        table
                .withNewRow()
                .withCell(new Date())
                .withCell("Col two")
                .withCell("BG Dark Green", tableCellStyle)
                .withCell("BG Dark Green + cols span", tableCellStyle, 2)
                .withCell(tableCellDate)
                .withNewRow()
                .withCell(new Date())
                .withCell("@Col two")
                .withCell("@BG Dark Green", tableCellStyle)
                .withCell("@BG Dark Green + cols span", tableCellStyle, 2)
                .withCell(tableCellDate);
        File file = ResourceUtils.newFileTargetResource("/tableFluentRowAndCellTest.xlsx");
        File outFile = ExporterUtil.getXlsxFile(table, file.getName());
        Files.move(outFile, file);
    }

}