package br.com.tecsinapse.exporter.converter;

import java.math.BigDecimal;

import com.google.common.base.Strings;

public class IntegerFromBigDecimalTableCellConverter implements
		TableCellConverter<Integer> {

	@Override
	public Integer apply(String input) {
		return Strings.isNullOrEmpty(input) ? null : new BigDecimal(input).intValue();
	}
}
