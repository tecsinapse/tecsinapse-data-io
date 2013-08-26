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
	 
    @Test
    public void testRowspanFirstLine() {
        Table t = new Table();
        t.addNewRow();
        TableCell l1 = new TableCell("Linha 1", TableCellType.HEADER);
        l1.setColspan(6);
        t.add(l1);
		  
        TableCell l12 = new TableCell("Linha 1/2", TableCellType.HEADER);
        l12.setRowspan(2);
		  t.add(l12);
        
		  t.addNewRow();
        TableCell l2 = new TableCell("Linha 2", TableCellType.HEADER);
        l2.setColspan(6);
        t.add(l2);
        
		  
        String text = t.getStringMatrixAsString(t.toStringMatrix());
        Assert.assertEquals(text, 
				  "|Linha 1||||||Linha 1/2\n" + 
				  "|Linha 2||||||\n");
    }

}
