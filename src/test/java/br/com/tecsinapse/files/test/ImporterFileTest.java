package br.com.tecsinapse.files.test;


import static org.testng.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Charsets;
import org.joda.time.LocalDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.FileType;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import br.com.tecsinapse.exporter.importer.ExcelParser;
import br.com.tecsinapse.exporter.importer.Importer;
import br.com.tecsinapse.exporter.importer.ImporterXLSXType;

public class ImporterFileTest {

    public static final class LocalDateConverter implements TableCellConverter<LocalDate> {
        @Override
        public LocalDate apply(String input) {
//TODO ExcelParser.dateStringPattern > Importer.dateStringPattern unificar confs em Importer ou ImporterConfigurations para utilizaçao em ExcelParser e CsvParser
//            csv texto puro dd/MM/yyyy
//            xls dd/MM/yyyy 03/01/2014
//            xlsx MM/dd/yyyy 01/03/14
            return null;
        }
    }

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/mock/" + name).toURI());
    }

    private List<FileBean> getEsperados() {
        //Cidade;Estado;Data;;Número
        List<FileBean> esperados = new ArrayList<>();
        //Pernambuco;PE;01/01/14;;10
        esperados.add(new FileBean("Pernambuco", "PE", new LocalDate(2014, 1, 1), "", 10, new BigDecimal("10.9"), 10));
        //Campo Grande;MS;02/01/14;;11
        esperados.add(new FileBean("Campo Grande", "MS", new LocalDate(2014, 1, 2), "", 11, new BigDecimal("11.8"), 11));
        //Rio de Janeiro;RJ;03/01/14;;12
        esperados.add(new FileBean("Rio de Janeiro", "RJ", new LocalDate(2014, 1, 3), "", 12, new BigDecimal("12.7"), 12));
        //São Paulo;SP;04/01/14;;13
        esperados.add(new FileBean("São Paulo", "SP", new LocalDate(2014, 1, 4), "", 13, new BigDecimal("13.6"), 13));
        //São Paulo;SP;05/01/14;;14
        esperados.add(new FileBean("São Paulo", "SP", new LocalDate(2014, 1, 5), "", 14, new BigDecimal("14.5"), 14));
        return esperados;
    }

    @DataProvider(name = "arquivos")
    public Object[][] arquivos() throws URISyntaxException {
        List<FileBean> esperados = getEsperados();


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
        try (final Importer<FileBean> importer = new Importer<>(FileBean.class, Charsets.UTF_8, arquivo)) {
            importer.setAfterLine(afterLine);

            assertEquals(importer.getFileType(), esperadoFileType);

            final List<FileBean> beans = importer.parse();
            for (int i = 0; i < beans.size(); i++) {
                assertFileBeanEquals(beans.get(i), esperados.get(i));
            }
        }
    }

    @DataProvider(name = "arquivosLastSheet")
    public Object[][] arquivosLastSheet() throws URISyntaxException {
        final List<FileBean> esperados = getEsperados();

        List<FileBean> reverse = getEsperados();
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
        try (final ExcelParser<FileBean> excelParser = new ExcelParser<>(FileBean.class, arquivo, afterLine, lastSheet, ImporterXLSXType.DEFAULT)) {

            final List<FileBean> beans = excelParser.parse();
            for (int i = 0; i < beans.size(); i++) {
                assertFileBeanEquals(beans.get(i), esperados.get(i));
            }
        }
    }

    @DataProvider(name = "arquivosSheet")
    public Object[][] arquivosSheet() throws URISyntaxException {
        final List<FileBean> esperados = getEsperados();

        List<FileBean> reverse = getEsperados();

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
        try (final ExcelParser<FileBean> excelParser = new ExcelParser<>(FileBean.class, arquivo, afterLine)) {
            for (int sheetNumber = 0; sheetNumber < sheets.size(); sheetNumber++) {
                excelParser.setSheetNumber(sheetNumber);
                final List<FileBean> esperados = sheets.get(sheetNumber);

                final List<FileBean> beans = excelParser.parse();
                for (int i = 0; i < beans.size(); i++) {
                    final FileBean atual = beans.get(i);
                    final FileBean esperado = esperados.get(i);
                    assertFileBeanEquals(atual, esperado);
                }
            }
        }
    }

    private void assertFileBeanEquals(FileBean atual, FileBean esperado) {
        assertEquals(atual.cidade, esperado.cidade);
        assertEquals(atual.estado, esperado.estado);
        assertEquals(atual.data, esperado.data);
        assertEquals(atual.vazia, esperado.vazia);
        assertEquals(atual.inteiro, esperado.inteiro);
        assertEquals(atual.decimal.compareTo(esperado.decimal), 0);
        assertEquals(atual.numeroInteger, esperado.numeroInteger);
    }
}
