/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.files.test;

import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_FOOTER;
import static br.com.tecsinapse.dataio.style.Style.TABLE_CELL_STYLE_HEADER;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.dataio.ExporterFormatter;
import br.com.tecsinapse.dataio.Table;
import br.com.tecsinapse.dataio.TableCell;
import br.com.tecsinapse.dataio.exceptions.ExporterException;
import br.com.tecsinapse.dataio.importer.parser.SpreadsheetParser;
import br.com.tecsinapse.dataio.util.CsvUtil;
import br.com.tecsinapse.dataio.util.ExporterUtil;

public class ExporterFileTest {

    private static final List<Locale> LOCALES = Arrays.asList(new Locale("pt", "BR"), ENGLISH, FRENCH, Locale.getDefault());

    private static ExporterFormatter exporterFormatter = ExporterFormatter.ENGLISH;

    @DataProvider(name = "beans")
    public Object[][] beans() {
        final List<FileBean> beans = FileBean.getBeans();

        //melhorar e unificar api de exportação
        final java.util.function.Function<Table, File> csvExport = table -> {
            try {
                final File csv = File.createTempFile("csv", ".csv");
                csv.deleteOnExit();

                ExporterUtil.writeCsvToOutput(table, StandardCharsets.ISO_8859_1.displayName(), new FileOutputStream(csv));

                return csv;
            } catch (IOException e) {
                throw new ExporterException(e);
            }
        };
        final java.util.function.Function<File, List<List<String>>> csvLines = input -> {
            try {
                final List<String> strings = CsvUtil.processCSV(new FileInputStream(input), StandardCharsets.ISO_8859_1);
                return strings.stream().map(str -> Arrays.asList(str.split(";"))).collect(Collectors.toList());
            } catch (IOException e) {
                throw new ExporterException(e);
            }
        };

        final java.util.function.Function<File, List<List<String>>> excelLines = file -> {
            try (final SpreadsheetParser<?> parser = new SpreadsheetParser<>(null, file)) {
                parser.setExporterFormatter(exporterFormatter);
                parser.setHeadersRows(0);
                return parser.getLines();
            } catch (Exception e) {
                throw new ExporterException(e);
            }
        };

        final java.util.function.Function<Table, File> xlsExport = toWorkbookFunction(new Supplier<Workbook>() {
            @Override
            public Workbook get() {
                return new HSSFWorkbook();
            }
        });
        final java.util.function.Function<Table, File> xlsxExport = toWorkbookFunction(new Supplier<Workbook>() {
            @Override
            public Workbook get() {
                return new XSSFWorkbook();
            }
        });
        final Function<Table, File> sxlsxExport = toWorkbookFunction(new Supplier<Workbook>() {
            @Override
            public Workbook get() {
                return new SXSSFWorkbook();
            }
        });

        return new Object[][]{
                {beans, csvExport, csvLines, "#.#", ""},
                {beans, xlsExport, excelLines, "#.#", ""},
                {beans, xlsxExport, excelLines, ".#", ""},
                {beans, sxlsxExport, excelLines, ".#", ""}
        };
    }

    private java.util.function.Function<Table, File> toWorkbookFunction(final java.util.function.Supplier<Workbook> supplier) {
        return table -> {
            try {
                String ext = supplier.get() instanceof HSSFWorkbook ? ".xls" : ".xlsx";
                final File file = File.createTempFile("xls", ext);
                file.deleteOnExit();

                try (final FileOutputStream out = new FileOutputStream(file)) {
                    table.toWorkBook(supplier.get())
                            .write(out);
                    return file;
                }
            } catch (IOException e) {
                throw new ExporterException(e);
            }
        };
    }

    @Test(dataProvider = "beans")
    public void testExporter(
            List<FileBean> beans,
            Function<Table, File> toFile, Function<File, List<List<String>>> toLines,
            String decimalPattern, String nullValue) throws IOException {
        for (Locale locale : LOCALES) {
            Locale.setDefault(locale);
            final String dataPattern = "dd/MM/yyyy";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dataPattern);
            final DecimalFormat decimalFormat = new DecimalFormat(decimalPattern, new DecimalFormatSymbols(locale));
            exporterFormatter = new ExporterFormatter("dd/MM/yyyy HH:mm", dataPattern, "HH:mm", decimalPattern, "#,###", "R$ #,##0.00", locale);

            final Table table = new Table();

            //header
            table.addNewRow();
            table.add("Cidade", TABLE_CELL_STYLE_HEADER);
            table.add("Estado", TABLE_CELL_STYLE_HEADER);
            table.add("Data", TABLE_CELL_STYLE_HEADER);
            table.add("", TABLE_CELL_STYLE_HEADER);
            table.add("Inteiro", TABLE_CELL_STYLE_HEADER);
            table.add("Decimal", TABLE_CELL_STYLE_HEADER);

            for (FileBean bean : beans) {
                //body
                table.addNewRow();
                table.add(bean.cidade);
                table.add(bean.estado);
                table.add(bean.data == null ? "" : dateTimeFormatter.format(bean.data));// exportar em formato data
                table.add("");
                table.add(bean.inteiro);
                table.add(bean.decimal);
            }

            //teste
            table.addNewRow();
            table.add("");
            table.add(" ");
            table.add((String) null);
            table.add((Number) null);
            table.add("last", TABLE_CELL_STYLE_FOOTER);
            table.add(new TableCell("last tc", TABLE_CELL_STYLE_FOOTER));

            table.setExporterFormatter(exporterFormatter);
            final File file = toFile.apply(table);
            final List<List<String>> lines = toLines.apply(file);
            String fileName = file.getName();

            //asserts
            assertEquals(1 + beans.size() + 1, lines.size());

            for (int i = 0; i < lines.size(); i++) {
                final List<String> row = lines.get(i);
                assertEquals(row.size(), 6);

                if (i == 0) {//header
                    assertEquals(row.get(0), "Cidade", fileName);
                    assertEquals(row.get(1), "Estado", fileName);
                    assertEquals(row.get(2), "Data", fileName);
                    assertEquals(row.get(3), "", fileName);
                    assertEquals(row.get(4), "Inteiro", fileName);
                    assertEquals(row.get(5), "Decimal", fileName);
                    continue;
                }

                if (i <= beans.size()) { // body
                    final FileBean bean = beans.get(i - 1);
                    assertEquals(row.get(0), bean.cidade, fileName);
                    assertEquals(row.get(1), bean.estado, fileName);
                    assertEquals(row.get(2), bean.data == null ? "" : dateTimeFormatter.format(bean.data), fileName);
                    assertEquals(row.get(3), "", fileName);
                    assertEquals(row.get(4), decimalFormat.format(bean.inteiro), fileName);
                    assertEquals(row.get(5), decimalFormat.format(bean.decimal), fileName);

                } else {//row teste
                    assertEquals(row.get(0), "", fileName);
                    assertEquals(row.get(1), " ", fileName);
                    assertEquals(row.get(2), nullValue, fileName);
                    assertEquals(row.get(3), nullValue, fileName);
                    assertEquals(row.get(4), "last", fileName);
                    assertEquals(row.get(5), "last tc", fileName);
                }
            }
        }
    }
}
