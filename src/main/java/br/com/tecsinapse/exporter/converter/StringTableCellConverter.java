package br.com.tecsinapse.exporter.converter;

import com.google.common.base.Strings;

public class StringTableCellConverter implements TableCellConverter<String> {

    @Override
    public String apply(String input) {
        if(Strings.isNullOrEmpty(input)) {
            return "";
        }
        return input.trim();
    }
}
