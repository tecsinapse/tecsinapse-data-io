/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.deprecated;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import br.com.tecsinapse.datasources.DataParser;
import br.com.tecsinapse.datasources.FileDataParser;
import br.com.tecsinapse.datasources.SpreadsheetDs;
import br.com.tecsinapse.exporter.ExcelUtil;
import br.com.tecsinapse.exporter.ExporterFormatter;
import br.com.tecsinapse.exporter.ResourceUtils;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.importer.ExcelParser;
import br.com.tecsinapse.exporter.type.FileType;

@Deprecated
public class ExcelParserTest extends SpreadsheetDs {

    @Test(dataProvider = "spreadsheetDs")
    public void spreadsheetParserTest(FileDataParser<DataParser> fileDataParser, ExporterFormatter exporterFormatter, Locale locale) throws Exception {
        Locale.setDefault(locale);
        ExcelParser<DataParser> parser = new ExcelParser<>(DataParser.class, fileDataParser.getFile());
        parser.setHeadersRows(1);
        parser.setExporterFormatter(exporterFormatter);
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
            ExcelUtil.getXlsxFile(table, file.getAbsolutePath()).deleteOnExit();
        }
    }

}