package br.com.tecsinapse.exporter.converter;

import java.math.BigDecimal;

import com.google.common.base.Strings;

public class LongFromDecimalTableCellConverter implements TableCellConverter<Long> {
	@Override
	public Long apply(String input) {
		return Strings.isNullOrEmpty(input) ? null : new BigDecimal(input).longValue();
	}
}
