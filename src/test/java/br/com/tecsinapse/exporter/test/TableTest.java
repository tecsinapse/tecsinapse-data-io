/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.test;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.ExporterFormatter;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.style.TableCellStyle;

public class TableTest {

    @Test
    public void testAutoSizeColumn_ComprovaErroMetodoAutoSizeColumn_de_Sheet_QuandoUltimasLinhasSaoVazias() {
        final int tamanhoIncorretoColuna = 236;
        final int tamanhoCorretoColuna = 2048;

        Table t = new Table();
        //após escrever certo número de linhas com o conteúdo vazio,
        //a coluna não é ajustada para o tamanho equivalente a linha
        //com maior número de caracteres
        for (int i = 1; i < 130; i++) {
            t.addNewRow();
            if (i < 20) {
                t.add("Teste erro " + i);
            } else {
                t.add(" ");
            }
        }

        Workbook wb = t.toWorkBook(new SXSSFWorkbook());
        Sheet sheet = wb.getSheetAt(0);

//		alterado o modo de validação devido a diferneça de plataformas(Windows, Linux, Mac) esses valores podem mudar, porém devem respeitar o mínimo. Por isso usado assertTrue e não assertEquals
        Assert.assertTrue(sheet.getColumnWidth(0) >= tamanhoIncorretoColuna);
        Assert.assertTrue(sheet.getColumnWidth(1) >= tamanhoCorretoColuna);
    }

    @Test
    public void testAutoSizeColumn_GeraTamanhoConformeMaiorQuantidadeCaracteresColuna() {
        final int tamanhoUmCaracter = 256;
        final int tamanhoDefaultColuna = 2048;
        final int maiorNumeroCaracteresColuna = 13;

        Table t = new Table() {
            @Override
            public boolean isAutoSizeColumnSheet() {
                return false;
            }
        };

        for (int i = 1; i < 130; i++) {
            t.addNewRow();
            if (i < 20) {
                t.add("Teste erro " + i);
            } else {
                t.add(" ");
            }
        }

        Workbook wb = t.toWorkBook(new SXSSFWorkbook());
        Sheet sheet = wb.getSheetAt(0);
        Assert.assertEquals(sheet.getColumnWidth(0), maiorNumeroCaracteresColuna * tamanhoUmCaracter);
        Assert.assertEquals(sheet.getColumnWidth(1), tamanhoDefaultColuna);
    }


    @Test
    public void testColspanFirstLine() {
        Table t = new Table();
        t.addNewRow();
        TableCell cellDados = new TableCell("Coluna 1", TableCellStyle.HEADER);
        cellDados.setColspan(6);
        t.add(cellDados);

        TableCell cellComparacao = new TableCell("Coluna 2", TableCellStyle.HEADER);
        cellComparacao.setColspan(2);
        t.add(cellComparacao);

        String text = t.getStringMatrixAsString(t.toStringMatrix());
        Assert.assertEquals(text, "|Coluna 1||||||Coluna 2|\n");
    }

    @Test
    public void testRowspanFirstLine() {
        Table t = new Table();
        t.addNewRow();
        TableCell l1 = new TableCell("Linha 1", TableCellStyle.HEADER);
        l1.setColspan(6);
        t.add(l1);

        TableCell l12 = new TableCell("Linha 1/2", TableCellStyle.HEADER);
        l12.setRowspan(2);
        t.add(l12);

        t.addNewRow();
        TableCell l2 = new TableCell("Linha 2", TableCellStyle.HEADER);
        l2.setColspan(6);
        t.add(l2);


        String text = t.getStringMatrixAsString(t.toStringMatrix());
        Assert.assertEquals(text,
                "|Linha 1||||||Linha 1/2\n" +
                        "|Linha 2||||||\n");
    }

    @Test
    public void testRowAndColspanFirstLine() {
        Table t = new Table();
        t.addNewRow();
        TableCell l1 = new TableCell("Linha/Coluna 1", TableCellStyle.HEADER);
        l1.setColspan(2);
        l1.setRowspan(2);
        t.add(l1);

        t.add(new TableCell("Linha 1", TableCellStyle.HEADER));

        t.addNewRow();
        t.add(new TableCell("Linha 2", TableCellStyle.HEADER));


        String text = t.getStringMatrixAsString(t.toStringMatrix());
        Assert.assertEquals(text,
                "|Linha/Coluna 1|Linha 1|\n" +
                        "|Linha 2||\n");
    }

    @Test
    public void testNumber() {
        Table t = new Table();
        t.addNewRow();
        t.add(new TableCell(1, TableCellStyle.HEADER));
        t.add(new TableCell(2.2, TableCellStyle.HEADER));

        t.addNewRow();
        t.add(10);
        t.add(10.2);
        t.setExporterFormatter(ExporterFormatter.ENGLISH);
        String text = t.getStringMatrixAsString(t.toStringMatrix());
        Assert.assertEquals(text,
                "|1|2.2\n" +
                        "|10|10.2\n");
    }

}
