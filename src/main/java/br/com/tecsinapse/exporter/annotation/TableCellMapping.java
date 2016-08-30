/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import br.com.tecsinapse.exporter.converter.Converter;
import br.com.tecsinapse.exporter.converter.StringTableCellConverter;
import br.com.tecsinapse.exporter.converter.group.Default;

@Retention(RUNTIME)
@Target({METHOD})
public @interface TableCellMapping {

    int columnIndex();

    Class<? extends Converter<?, ?>> converter() default StringTableCellConverter.class;

    Class<?>[] groups() default {Default.class};

}
