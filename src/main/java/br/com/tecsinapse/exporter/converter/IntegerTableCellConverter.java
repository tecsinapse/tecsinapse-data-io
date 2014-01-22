package br.com.tecsinapse.exporter.converter;


import com.google.common.base.Strings;

public class IntegerTableCellConverter implements TableCellConverter<Integer> {

    @Override
    public Integer apply(String input) {
        return Strings.isNullOrEmpty(input) ? null : Integer.valueOf(input);
    }
}
