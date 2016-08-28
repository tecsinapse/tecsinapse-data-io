/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.type;

public enum SeparatorType {

    TAB("\t"),
    NONE(""),
    SPACE(" "),
    SEMICOLON(";"),
    COMMA(",");

    private final String separator;

    SeparatorType(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }

}
