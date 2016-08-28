/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

/**
 * Does some thing in old style. It will be removed in version 2.0.0
 *
 * @deprecated use {@link br.com.tecsinapse.exporter.converter.FromDateConverter} to load date cells from xls ou xlsx,
 * {@link br.com.tecsinapse.exporter.converter.FromNumberConverter} to load numbers cells from xls ou xlsx
 * {@link br.com.tecsinapse.exporter.converter.FromStringConverter} to load generic cells from xls ou xlsx
 */
@Deprecated
public interface TableCellConverter<T> extends Converter<String, T> {

}
