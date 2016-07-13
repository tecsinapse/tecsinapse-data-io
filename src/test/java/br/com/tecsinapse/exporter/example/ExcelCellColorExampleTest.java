/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.poi.hssf.util.HSSFColor;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.ExcelUtil;
import br.com.tecsinapse.exporter.ResourceUtils;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.style.SpreadsheetCellStyle;
import br.com.tecsinapse.exporter.util.ExportHtml;

public class ExcelCellColorExampleTest {

    @Test
    public void generateExample() throws IOException {
        Table table = new Table();

        int line = 0;
        for (HSSFColor color : HSSFColor.getIndexHash().values()) {
            table.addNewRow();
            line++;
            TableCell tableCell = new TableCell(String.format("Line %d - Color: %s", line, color.getClass().getSimpleName()));
            tableCell.setSpreadsheetCellStyle(new SpreadsheetCellStyle(color));
            table.add(tableCell);
            for (int i = 0; i < 10; i++) {
                tableCell = new TableCell(String.format("Col %d", i));
                tableCell.setSpreadsheetCellStyle(new SpreadsheetCellStyle(color));
                table.add(tableCell);

            }
        }
        String xlsx = "XLSX-cell-color.xlsx";
        Files.move(ExcelUtil.getXlsFile(table, xlsx).toPath(), ResourceUtils.newFileTargetResource(xlsx).toPath(), StandardCopyOption.REPLACE_EXISTING);

        String xls = "XLS-cell-color.xls";
        Files.move(ExcelUtil.getXlsFile(table, xls).toPath(), ResourceUtils.newFileTargetResource(xls).toPath(), StandardCopyOption.REPLACE_EXISTING);

        File htmlOutput = ResourceUtils.newFileTargetResource("HTML-cell-color.html");
        ExportHtml.newInstance().toHtml(table, htmlOutput);
    }


}
