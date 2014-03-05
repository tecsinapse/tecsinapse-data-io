package br.com.tecsinapse.exporter.converter;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class LocalDateTableCellConverter implements TableCellConverter<LocalDate> {

	private static final LocalDateTimeTableCellConverter converter = new LocalDateTimeTableCellConverter();
	
	public LocalDate apply(String input) {
		LocalDateTime value = converter.apply(input);
		return value != null ? value.toLocalDate(): null;
	}

}
