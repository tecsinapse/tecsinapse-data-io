/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.converter;

import com.google.common.base.Strings;

public class StringTableCellConverter implements FromStringConverter<String> {

    @Override
    public String apply(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return "";
        }
        return input.trim();
    }

}
