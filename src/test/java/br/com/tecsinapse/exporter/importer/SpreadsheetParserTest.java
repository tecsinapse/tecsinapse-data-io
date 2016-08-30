/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tecsinapse.ResourceFiles;
import br.com.tecsinapse.datasources.DataParser;
import br.com.tecsinapse.datasources.FileDataParser;
import br.com.tecsinapse.datasources.SpreadsheetDs;
import br.com.tecsinapse.exporter.ExporterFormatter;
import br.com.tecsinapse.exporter.ResourceUtils;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.importer.parser.SpreadsheetParser;
import br.com.tecsinapse.exporter.type.FileType;
import br.com.tecsinapse.exporter.util.ExporterUtil;

public class SpreadsheetParserTest extends SpreadsheetDs {

    @Test(dataProvider = "spreadsheetDs")
    public void spreadsheetParserTest(FileDataParser<DataParser> fileDataParser, ExporterFormatter exporterFormatter, Locale locale) throws Exception {
        Locale.setDefault(locale);
        SpreadsheetParser<DataParser> parser = new SpreadsheetParser<>(DataParser.class, fileDataParser.getFile());
        parser.setHeadersRows(1);
        parser.setExporterFormatter(exporterFormatter);
        Assert.assertEquals(parser.getExporterFormatter(), exporterFormatter);
        Assert.assertEquals(parser.getFileType(), fileDataParser.getExpectedFileType());
        fileDataParser.setParser(parser);
        fileDataParser.test();
    }

    @Test(dataProvider = "spreadsheetDs")
    public void spreadsheetGenerateExcelTest(FileDataParser<DataParser> fileDataParser, ExporterFormatter exporterFormatter, Locale locale) throws Exception {
        Locale.setDefault(locale);
        Table table = new Table();
        table.setExporterFormatter(exporterFormatter);
        for (Entry<Integer, List<DataParser>> entry : fileDataParser.getSheets().entrySet()) {
            for(DataParser dataParser : entry.getValue()) {
                table.addNewRow();
                table.add(new TableCell(dataParser.getDate()));
                table.add(new TableCell(dataParser.getDateTime()));
                table.add(new TableCell(dataParser.getDecimal()));
                table.add(new TableCell(dataParser.getInteger()));
                table.add(new TableCell(dataParser.getString()));
                table.add(new TableCell(dataParser.getEmpty()));
                table.add(new TableCell(dataParser.getTime()));
            }
        }

        FileType fileType = FileType.getFileType(fileDataParser.getFile().getName());
        if (fileType == FileType.XLSX || fileType == FileType.XLSX) {
            File file = ResourceUtils.newFileTargetResource(exporterFormatter.getLocale().getDisplayLanguage() + "-teste." + fileType.name().toLowerCase());
            ExporterUtil.getXlsxFile(table, file.getAbsolutePath()).deleteOnExit();
        }
    }

    @Test(expectedExceptions = {Exception.class})
    public void throwIOException() throws Exception {
        SpreadsheetParser<DataParser> parser = new SpreadsheetParser<>(DataParser.class, new ByteArrayInputStream(new byte[0]), FileType.XLSX);
        Assert.assertNull(parser.getWorkbook());
    }

    @Test
    public void parseListString() throws Exception {
        SpreadsheetParser<DataParser> parser = new SpreadsheetParser<>(DataParser.class, ResourceFiles.EXCEL_XLSX.getFile());
        parser.setHeadersRows(1);
        List<List<String>> rows = parser.getLines();
        Assert.assertEquals(rows.size(), 2);
    }

}