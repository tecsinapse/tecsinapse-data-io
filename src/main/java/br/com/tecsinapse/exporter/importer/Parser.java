/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Parser<T> extends Closeable {

    List<T> parse() throws IllegalAccessException, InstantiationException, InvocationTargetException, Exception;

    /**
     * Quantidade de sheets na planilha
     * Para csv sempre retorna 1, apenas para centralizar processamento pelo Importer idependente do arquivo
     *
     * @return quantidade de sheets
     */
    int getNumberOfSheets();

    void setParserFormatter(ParserFormatter parserFormatter);

}
