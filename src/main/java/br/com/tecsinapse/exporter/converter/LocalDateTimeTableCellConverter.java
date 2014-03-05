package br.com.tecsinapse.exporter.converter;

import org.joda.time.LocalDateTime;

import com.google.common.base.Strings;

public class LocalDateTimeTableCellConverter implements
		TableCellConverter<LocalDateTime> {

	public LocalDateTime apply(String input) {
		return Strings.isNullOrEmpty(input) ? null : LocalDateTime.parse(input);
	}

}
