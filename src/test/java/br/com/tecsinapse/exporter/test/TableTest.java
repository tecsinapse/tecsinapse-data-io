package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.TableCellType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TableTest {

    @Test
    public void testColspanFirstLine() {
        Table t = new Table();
        t.addNewRow();
        TableCell cellDados = new TableCell("Coluna 1", TableCellType.HEADER);
        cellDados.setColspan(6);
        t.add(cellDados);

        TableCell cellComparacao = new TableCell("Coluna 2", TableCellType.HEADER);
        cellComparacao.setColspan(2);
        t.add(cellComparacao);

        String text = t.getStringMatrixAsString(t.toStringMatrix());
        Assert.assertEquals(text, "|Coluna 1||||||Coluna 2|\n");

    }

}
