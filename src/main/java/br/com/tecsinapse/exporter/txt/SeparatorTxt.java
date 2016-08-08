/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.txt;

/**
 * This class is moved. It will be removed in version 2.0.0
 *
 * @deprecated use {@link br.com.tecsinapse.exporter.type.SeparatorType}
 */
@Deprecated
public enum SeparatorTxt {

    TAB("\t"),
    NONE(""),
    SPACE(" "),
    SEMICOLON(";"),
    COMMA(",");

    private final String separator;

    SeparatorTxt(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }

}
