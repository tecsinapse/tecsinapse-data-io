/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.example;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.testng.annotations.Test;

import br.com.tecsinapse.dataio.ResourceUtils;
import br.com.tecsinapse.dataio.Table;
import br.com.tecsinapse.dataio.TableCell;
import br.com.tecsinapse.dataio.style.Style;
import br.com.tecsinapse.dataio.style.TableCellStyle;
import br.com.tecsinapse.dataio.util.ExportHtml;
import br.com.tecsinapse.dataio.util.ExporterUtil;

public class ExcelCellColorExampleTest {

    @Test
    public void generateExample() throws IOException {
        Table table = new Table();

        int line = 0;
        List<HSSFColor> colors = new ArrayList<>(HSSFColor.getIndexHash().values());
        colors.add(new HSSFColor.AUTOMATIC());

        HSSFColor cBlue = table.newCustomColor(new HSSFColor.AQUA(), new java.awt.Color(0,0,255));
        HSSFColor cGreen = table.newCustomColor(new HSSFColor.RED(), new java.awt.Color(0,255,0));
        colors.add(cGreen);
        colors.add(cBlue);
        for (HSSFColor color : colors) {
            table.addNewRow();
            line++;
            TableCell tableCell = new TableCell(String.format("Line %d - Color: %s", line, color.getClass().getSimpleName()));
            TableCellStyle style = new TableCellStyle(color);
            tableCell.setTableCellStyle(style);
            table.add(tableCell);
            for (int i = 0; i < 10; i++) {
                tableCell = new TableCell(String.format("Col %d", i));
                tableCell.setTableCellStyle(style);
                style.setBold(true);
                table.add(tableCell);
            }
        }
        String xlsx = "XLSX-cell-color.xlsx";
        Files.move(ExporterUtil.getXlsxFile(table, xlsx).toPath(), ResourceUtils.newFileTargetResource(xlsx).toPath(), StandardCopyOption.REPLACE_EXISTING);

        String sxlsx = "XLSX-cell-color-streaming.xlsx";
        Files.move(ExporterUtil.getSxlsxFile(table, sxlsx).toPath(), ResourceUtils.newFileTargetResource(sxlsx).toPath(), StandardCopyOption.REPLACE_EXISTING);

        String xls = "XLS-cell-color.xls";
        Files.move(ExporterUtil.getXlsFile(table, xls).toPath(), ResourceUtils.newFileTargetResource(xls).toPath(), StandardCopyOption.REPLACE_EXISTING);

        File htmlOutput = ResourceUtils.newFileTargetResource("HTML-cell-color.html");
        ExportHtml.newInstance(Charset.forName("UTF-8")).toHtml(table, htmlOutput);
    }

    @Test
    public void generateManySheetsExample() throws IOException {
        List<Table> tables = new ArrayList<>();
        for (int s = 1; s <= 5; s++) {
            Table table = new Table();
            table.setTitle(String.format("Plan %d", s));
            int line = 0;
            for (HSSFColor color : HSSFColor.getIndexHash().values()) {
                table.addNewRow();
                line++;
                TableCell tableCell = new TableCell(String.format("[Sheet %d] Line %d - Color: %s", s, line, color.getClass().getSimpleName()));
                TableCellStyle style = new TableCellStyle(color);
                tableCell.setTableCellStyle(style);
                table.add(tableCell);
                for (int i = 0; i < 10; i++) {
                    tableCell = new TableCell(String.format("Col %d", i));
                    tableCell.setTableCellStyle(style);
                    style.setBold(true);
                    table.add(tableCell);
                }
            }
            tables.add(table);
        }
        String xlsx = "XLSX-cell-color-many-sheets.xlsx";
        Files.move(ExporterUtil.getMoreThanOneSheetXlsxFile(tables, xlsx).toPath(), ResourceUtils.newFileTargetResource(xlsx).toPath(), StandardCopyOption.REPLACE_EXISTING);

        String xls = "XLS-cell-color-many-sheets.xls";
        Files.move(ExporterUtil.getMoreThanOneSheetXlsxFile(tables, xls).toPath(), ResourceUtils.newFileTargetResource(xls).toPath(), StandardCopyOption.REPLACE_EXISTING);

/*        File htmlOutput = ResourceUtils.newFileTargetResource("HTML-cell-color.html");
        ExportHtml.newInstance(Charset.forName("UTF-8")).toHtml(table, htmlOutput);*/
    }

    @Test
    public void generateExampleWrapText() throws IOException {

        final String longText = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos.";

        Table table = new Table();
        table.setAutoSizeColumnSheet(false);

        TableCellStyle styleNoWrapText = Style.TABLE_CELL_STYLE_BODY.duplicate();
        TableCellStyle styleWrapText = styleNoWrapText.duplicate();
        styleWrapText.setWrapText(true);

        table.addNewRow();
        table.add(longText, styleNoWrapText);
        table.add(longText, styleWrapText);
        table.add(1);
        table.add(15);
        table.add("Test");
        table.add("Test Test");
        table.add("Test Test Test");
        table.add("Test Test Test Test");

        String xlsx = "XLSX-wrap-text.xlsx";
        Files.move(ExporterUtil.getXlsxFile(table, xlsx).toPath(), ResourceUtils.newFileTargetResource(xlsx).toPath(), StandardCopyOption.REPLACE_EXISTING);

        String xls = "XLSX-wrap-text.xls";
        Files.move(ExporterUtil.getXlsFile(table, xls).toPath(), ResourceUtils.newFileTargetResource(xls).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


}
