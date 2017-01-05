package br.com.tecsinapse.exporter;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * Created by ruither on 03/10/16.
 */
public class TableCellTest {

	@Test
	public void testGetContentText() throws Exception {
		TableCell tc = new TableCell("<a href='/p/crm/relatorio/relatorio-acompanhamento/dealer/36470?dswid=9593&dsrid=9593'><span class='icon-plus' /> </a> Agulhas Negras - 23 (36470)", ContentType.HTML);

		String contentSemHtml = tc.getContentText();

		assertEquals("Agulhas Negras - 23 (36470)", contentSemHtml);
	}
}