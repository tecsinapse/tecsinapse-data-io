package br.com.tecsinapse.exporter.converter;

import java.math.BigDecimal;

import com.google.common.base.Strings;

public class BigDecimalTableCellConverter implements TableCellConverter<BigDecimal> {

	public BigDecimal apply(String input) {
		return Strings.isNullOrEmpty(input) ? null : new BigDecimal(input);
	}

}
