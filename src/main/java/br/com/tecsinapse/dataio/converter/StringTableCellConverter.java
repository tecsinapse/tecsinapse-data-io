/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.converter;

import br.com.tecsinapse.dataio.util.CommonUtils;

public class StringTableCellConverter implements FromStringConverter<String> {

    @Override
    public String apply(String input) {
        if (CommonUtils.isNullOrEmpty(input)) {
            return "";
        }
        return input.trim();
    }

}
