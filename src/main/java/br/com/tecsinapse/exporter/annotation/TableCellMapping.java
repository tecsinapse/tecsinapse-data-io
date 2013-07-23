package br.com.tecsinapse.exporter.annotation;

import br.com.tecsinapse.exporter.converter.StringTableCellConverter;
import br.com.tecsinapse.exporter.converter.TableCellConverter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD})
public @interface TableCellMapping {

    int columnIndex();

    Class<? extends TableCellConverter> converter() default StringTableCellConverter.class;
}
