package br.com.tecsinapse.exporter.converter;

import com.google.common.base.Function;

public interface TableCellConverter<T> extends Function<String, T> {

}
