/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import br.com.tecsinapse.dataio.type.CellType;

public class TableCellTest {

	@Test
	public void testGetContentText() throws Exception {
		TableCell tc = new TableCell("<a href='/p/crm/relatorio/relatorio-acompanhamento/dealer/36470?dswid=9593&dsrid=9593'><span class='icon-plus' /> </a> Agulhas Negras - 23 (36470)", CellType.HTML_TYPE);

		String contentSemHtml = tc.getContentText();

		assertEquals("Agulhas Negras - 23 (36470)", contentSemHtml);
	}
}