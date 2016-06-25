/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.files.test;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;

import br.com.tecsinapse.exporter.CSVUtil;
import br.com.tecsinapse.exporter.ExcelUtil;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.TableCellType;
import br.com.tecsinapse.exporter.importer.ExcelParser;

public class ExporterFileTest {

    private static final List<Locale> LOCALES = Arrays.asList(new Locale("pt","BR"), ENGLISH, FRENCH, Locale.getDefault());

    @DataProvider(name = "beans")
    public Object[][] beans() {
        final List<FileBean> beans = FileBean.getBeans();

        //melhorar e unificar api de exportação
        final Function<Table, File> csvExport = new Function<Table, File>() {
            @Override
            public File apply(Table table) {
                try {
                    final File csv = File.createTempFile("csv", ".csv");
                    csv.deleteOnExit();

                    ExcelUtil.exportCsv(table, Charsets.ISO_8859_1.displayName(), new FileOutputStream(csv));

                    return csv;
                } catch (IOException e) {
                    throw Throwables.propagate(e);
                }
            }
        };
        final Function<File, List<List<String>>> csvLines = new Function<File, List<List<String>>>() {
            @Override
            public List<List<String>> apply(File input) {
                try {
                    final List<String> strings = CSVUtil.processCSV(new FileInputStream(input), Charsets.ISO_8859_1);

                    return FluentIterable.from(strings).transform(new Function<String, List<String>>() {
                        @Override
                        public List<String> apply(String input) {
                            return Arrays.asList(input.split(";"));
                        }
                    }).toList();
                } catch (IOException e) {
                    throw Throwables.propagate(e);
                }
            }
        };

        final Function<File, List<List<String>>> excelLines = new Function<File, List<List<String>>>() {
            @Override
            public List<List<String>> apply(File file) {
                try (final ExcelParser<?> parser = new ExcelParser<>(null, file)) {
                    parser.setAfterLine(0);
                    return parser.getLines();
                } catch (Exception e) {
                    throw Throwables.propagate(e);
                }
            }
        };

        final Function<Table, File> xlsExport = toWorkbookFunction(new Supplier<Workbook>() {
            @Override
            public Workbook get() {
                return new HSSFWorkbook();
            }
        });
        final Function<Table, File> xlsxExport = toWorkbookFunction(new Supplier<Workbook>() {
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

    private Function<Table, File> toWorkbookFunction(final Supplier<Workbook> supplier) {
        return new Function<Table, File>() {
            @Override
            public File apply(Table table) {
                try {
                    final File file = File.createTempFile("xls", ".xls");
                    file.deleteOnExit();

                    try (final FileOutputStream out = new FileOutputStream(file)) {
                        table.toWorkBook(supplier.get())
                                .write(out);
                        return file;
                    }
                } catch (IOException e) {
                    throw Throwables.propagate(e);
                }
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
            final DecimalFormat decimalFormat = new DecimalFormat(decimalPattern, new DecimalFormatSymbols(ENGLISH));


            final Table table = new Table();

            //header
            table.addNewRow();
            table.add("Cidade", TableCellType.HEADER);
            table.add("Estado", TableCellType.HEADER);
            table.add("Data", TableCellType.HEADER);
            table.add("", TableCellType.HEADER);
            table.add("Inteiro", TableCellType.HEADER);
            table.add("Decimal", TableCellType.HEADER);

            for (FileBean bean : beans) {
                //body
                table.addNewRow();
                table.add(bean.cidade);
                table.add(bean.estado);
                table.add(bean.data == null ? "" : bean.data.toString(dataPattern));// exportar em formato data
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
            table.add("last", TableCellType.FOOTER);
            table.add(new TableCell("last tc", TableCellType.FOOTER));


            final File file = toFile.apply(table);
            final List<List<String>> lines = toLines.apply(file);

            //asserts
            assertEquals(1 + beans.size() + 1, lines.size());

            for (int i = 0; i < lines.size(); i++) {
                final List<String> row = lines.get(i);
                assertEquals(row.size(), 6);

                if (i == 0) {//header
                    assertEquals(row.get(0), "Cidade");
                    assertEquals(row.get(1), "Estado");
                    assertEquals(row.get(2), "Data");
                    assertEquals(row.get(3), "");
                    assertEquals(row.get(4), "Inteiro");
                    assertEquals(row.get(5), "Decimal");

                    continue;
                }

                if (i <= beans.size()) { // body
                    final FileBean bean = beans.get(i - 1);

                    assertEquals(row.get(0), bean.cidade);
                    assertEquals(row.get(1), bean.estado);
                    assertEquals(row.get(2), bean.data == null ? "" : bean.data.toString(dataPattern));
                    assertEquals(row.get(3), "");
                    assertEquals(row.get(4), decimalFormat.format(bean.inteiro));
                    assertEquals(row.get(5), decimalFormat.format(bean.decimal));

                } else {//row teste

                    assertEquals(row.get(0), "");
                    assertEquals(row.get(1), " ");
                    assertEquals(row.get(2), nullValue);
                    assertEquals(row.get(3), nullValue);
                    assertEquals(row.get(4), "last");
                    assertEquals(row.get(5), "last tc");
                }
            }
        }
    }
}
