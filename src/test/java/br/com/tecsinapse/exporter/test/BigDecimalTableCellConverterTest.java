/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.test;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.converter.BigDecimalTableCellConverter;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import static org.testng.Assert.*;

public class BigDecimalTableCellConverterTest {

	private static final String EMPTY_STRING = "";
	private static final String DECIMAL_VALUE = "257767336.0";
	
	TableCellConverter<BigDecimal> converter = new BigDecimalTableCellConverter();
	
	@Test
	public void test_convert() {
		BigDecimal result = converter.apply(DECIMAL_VALUE);
		
		assertEquals(result, new BigDecimal(DECIMAL_VALUE));
		
	}
	
	@Test
	public void test_null() {
		BigDecimal result = converter.apply(EMPTY_STRING);
		
		assertNull(result);
		
	}
	
}
