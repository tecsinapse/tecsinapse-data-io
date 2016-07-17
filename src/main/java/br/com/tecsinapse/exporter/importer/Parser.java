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

import br.com.tecsinapse.exporter.ImporterType;

public interface Parser<T> extends Closeable {

    List<T> parse() throws IllegalAccessException, InstantiationException, InvocationTargetException, Exception;

    void setParserFormatter(ParserFormatter parserFormatter);

    ParserFormatter getParserFormatter();

    void setHeadersRows(int headersRows);

    boolean isIgnoreBlankLinesAtEnd();

    void setIgnoreBlankLinesAtEnd(boolean ignoreBlankLinesAtEnd);

    List<List<String>> getLines() throws Exception;

    int getNumberOfSheets();

    void setSheetNumber(int sheetNumber);

    int getSheetNumber();

    void setSheetNumberAsFirstNotHidden();

    ImporterType getImporterType();
}
