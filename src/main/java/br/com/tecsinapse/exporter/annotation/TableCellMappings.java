package br.com.tecsinapse.exporter.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import br.com.tecsinapse.exporter.converter.StringTableCellConverter;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import br.com.tecsinapse.exporter.converter.group.Default;

@Retention(RUNTIME)
@Target({METHOD})
public @interface TableCellMappings {

    TableCellMapping[] value();
}
