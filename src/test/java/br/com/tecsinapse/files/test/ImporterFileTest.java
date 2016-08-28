/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.files.test;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;

import br.com.tecsinapse.exporter.converter.FromDateConverter;
import br.com.tecsinapse.exporter.importer.ExcelParser;
import br.com.tecsinapse.exporter.importer.Importer;
import br.com.tecsinapse.exporter.type.FileType;

public class ImporterFileTest {

    private static final String DD_MM_YYYY = "dd/MM/yyyy";

    private static final List<Locale> LOCALES = Arrays.asList(new Locale("pt", "BR"), ENGLISH, FRENCH, Locale.getDefault());

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/mock/" + name).toURI());
    }

    @DataProvider(name = "arquivos")
    public Object[][] arquivos() throws URISyntaxException {
        List<FileBean> esperados = FileBean.getBeans();

        return new Object[][]{
                {getFile("planilha.csv"), 1, FileType.CSV, esperados},
                {getFile("planilha.xls"), 1, FileType.XLS, esperados},
                {getFile("planilha.xlsx"), 1, FileType.XLSX, esperados},

                {getFile("planilha.csv"), 3, FileType.CSV, esperados.subList(2, esperados.size())},
                {getFile("planilha.xls"), 3, FileType.XLS, esperados.subList(2, esperados.size())},
                {getFile("planilha.xlsx"), 3, FileType.XLSX, esperados.subList(2, esperados.size())},
        };
    }

    @Test(dataProvider = "arquivos")
    public void validaArquivo(File arquivo, int afterLine, FileType esperadoFileType, List<FileBean> esperados) throws Exception {
        for (Locale locale : LOCALES) {
            Locale.setDefault(locale);
            try (final Importer<FileBean> importer = new Importer<>(FileBean.class, Charsets.UTF_8, arquivo)) {
                importer.setAfterLine(afterLine);

                assertEquals(importer.getFileType(), esperadoFileType);

                final List<FileBean> beans = importer.parse();
                for (int i = 0; i < beans.size(); i++) {
                    assertFileBeanEquals(beans.get(i), esperados.get(i), arquivo);
                }
            }
        }
    }

    @DataProvider(name = "arquivosLastSheet")
    public Object[][] arquivosLastSheet() throws URISyntaxException {
        final List<FileBean> esperados = FileBean.getBeans();

        List<FileBean> reverse = FileBean.getBeans();
        Collections.reverse(reverse);

        return new Object[][]{
                {getFile("planilha.xls"), false, 1, esperados},
                {getFile("planilha.xlsx"), false, 1, esperados},

                {getFile("planilha.xls"), false, 3, esperados.subList(2, esperados.size())},
                {getFile("planilha.xlsx"), false, 3, esperados.subList(2, esperados.size())},


                {getFile("planilha.xls"), true, 1, reverse},
                {getFile("planilha.xlsx"), true, 1, reverse},

                {getFile("planilha.xls"), true, 3, reverse.subList(2, reverse.size())},
                {getFile("planilha.xlsx"), true, 3, reverse.subList(2, reverse.size())},
        };
    }

    @Test(dataProvider = "arquivosLastSheet")
    public void validaLastSheet(File arquivo, boolean lastSheet, int afterLine, List<FileBean> esperados) throws Exception {
        for (Locale locale : LOCALES) {
            Locale.setDefault(locale);
            try (final ExcelParser<FileBean> excelParser = new ExcelParser<>(FileBean.class, arquivo, afterLine, lastSheet)) {
                //excelParser.setDateStringPattern(DD_MM_YYYY);

                final List<FileBean> beans = excelParser.parse();
                for (int i = 0; i < beans.size(); i++) {
                    assertFileBeanEquals(beans.get(i), esperados.get(i), arquivo);
                }
            }
        }
    }

    @DataProvider(name = "arquivosSheet")
    public Object[][] arquivosSheet() throws URISyntaxException {
        final List<FileBean> esperados = FileBean.getBeans();

        List<FileBean> reverse = FileBean.getBeans();

        Collections.reverse(reverse);
        List<FileBean> sheet2 = new ArrayList<>(reverse);
        Collections.swap(sheet2, 1, 3);

        return new Object[][]{
                {getFile("planilha.xls"), 1, Arrays.asList(esperados, sheet2, reverse)},
                {getFile("planilha.xlsx"), 1, Arrays.asList(esperados, sheet2, reverse)},
                {getFile("planilha.xls"), 3, Arrays.asList(esperados.subList(2, esperados.size()), sheet2.subList(2, sheet2.size()), reverse.subList(2, reverse.size()))},
                {getFile("planilha.xlsx"), 3, Arrays.asList(esperados.subList(2, esperados.size()), sheet2.subList(2, sheet2.size()), reverse.subList(2, reverse.size()))},
        };
    }

    @Test(dataProvider = "arquivosSheet")
    public void validaSheet(File arquivo, int afterLine, List<List<FileBean>> sheets) throws Exception {
        for (Locale locale : LOCALES) {
            Locale.setDefault(locale);
            try (final ExcelParser<FileBean> excelParser = new ExcelParser<>(FileBean.class, arquivo, afterLine)) {
                assertFileBeanEquals(excelParser, sheets, arquivo);
            }
        }
    }

    @Test(dataProvider = "arquivosSheet")
    public void validaSheetWithDate(File arquivo, int afterLine, List<List<FileBean>> sheets) throws Exception {
        for (Locale locale : LOCALES) {
            Locale.setDefault(locale);
            try (final ExcelParser<FileBean> excelParser = new ExcelParser<>(FileBean.class, arquivo, afterLine)) {
                assertFileBeanEquals(excelParser, sheets, arquivo);
            }
        }
    }

    private void assertFileBeanEquals(final ExcelParser<FileBean> excelParser, final List<List<FileBean>> sheets,
                                      final File arquivo) throws Exception {
        for (int sheetNumber = 0; sheetNumber < sheets.size(); sheetNumber++) {
            excelParser.setSheetNumber(sheetNumber);
            final List<FileBean> esperados = sheets.get(sheetNumber);

            final List<FileBean> beans = excelParser.parse();
            for (int i = 0; i < beans.size(); i++) {
                final FileBean atual = beans.get(i);
                final FileBean esperado = esperados.get(i);
                assertFileBeanEquals(atual, esperado, arquivo);
            }
        }
    }

    private void assertFileBeanEquals(FileBean atual, FileBean esperado, File file) {
        assertEquals(atual.cidade, esperado.cidade, file.getName());
        assertEquals(atual.estado, esperado.estado, file.getName());
        assertEquals(atual.data, esperado.data, file.getName());
        assertEquals(atual.vazia, esperado.vazia, file.getName());
        assertEquals(atual.inteiro, esperado.inteiro, file.getName());
        assertEquals(atual.decimal.compareTo(esperado.decimal), 0, file.getName());
        assertEquals(atual.numeroInteger, esperado.numeroInteger, file.getName());
    }

    @DataProvider(name = "filesWithHiddenSheet")
    public Object[][] filesWithHiddenSheet() throws URISyntaxException {
        return new Object[][]{
                {getFile("planilha-com-primeira-aba-invisivel.xls"), 1},
                {getFile("planilha-com-primeira-aba-invisivel.xlsx"), 2}
        };
    }

    @Test(dataProvider = "filesWithHiddenSheet")
    public void validateFilesWithHiddenSheet(File file, int expectedSheetNumber) throws IOException {
        try (final ExcelParser<FileBean> excelParser = new ExcelParser<>(FileBean.class, file, 1)) {
            assertEquals(excelParser.getSheetNumber(), 0);

            excelParser.setSheetNumberAsFirstNotHidden();
            assertEquals(excelParser.getSheetNumber(), expectedSheetNumber);

            try {
                excelParser.parse();
            } catch (Exception e) {
                Assert.fail("Fail while reading first not hidden sheet.", e);
            }
        }
    }

    public static final class LocalDateConverter implements FromDateConverter<LocalDate> {

        private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(DD_MM_YYYY);

        @Override
        public LocalDate apply(Date input) {
            return LocalDate.fromDateFields(input);
        }

        @Override
        public LocalDate apply(String input) {
            return FORMATTER.parseLocalDate(input);
        }

    }

}
