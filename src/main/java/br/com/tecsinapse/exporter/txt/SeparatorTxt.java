/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.txt;

/**
 * Representa os tipos de separadores possíveis para utilizar entre as colunas do arquivo {@code .txt} gerado.
 * <br><br>
 * <b>Separadores:</b>
 * <ul>
 * <li>TAB: \t (Tabulação)</li>
 * <li>NONE: '' (Vazio)</li>
 * <li>SPACE: ' ' (Espaço em branco)</li>
 * <li>SEMICOLON: ';' (Ponto e vírgula)</li>
 * <li>COMMA: ',' (Vírgula)</li>
 * </ul>
 */
public enum SeparatorTxt {

    TAB("\t"),
    NONE(""),
    SPACE(" "),
    SEMICOLON(";"),
    COMMA(",");

    SeparatorTxt(String separator) {
        this.separator = separator;
    }

    private final String separator;

    public String getSeparator() {
        return separator;
    }

}
