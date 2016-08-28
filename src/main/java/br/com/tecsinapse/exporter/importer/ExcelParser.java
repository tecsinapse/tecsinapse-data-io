/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import br.com.tecsinapse.exporter.converter.group.Default;
import br.com.tecsinapse.exporter.importer.parser.SpreadsheetParser;
import br.com.tecsinapse.exporter.type.FileType;

/**
 * Does some thing in old style. It will be removed in version 2.0.0
 *
 * @deprecated use {@link br.com.tecsinapse.exporter.importer.parser.SpreadsheetParser}
 */
@Deprecated
public class ExcelParser<T> extends SpreadsheetParser<T> {

    public ExcelParser(Class<T> clazz, File file) throws IOException {
        super(clazz, new FileInputStream(file), file.getName());
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine) throws IOException {
        super(clazz, new FileInputStream(file), file.getName());
        super.setHeadersRows(afterLine);
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine, boolean lastSheet) throws IOException {
        super(clazz, new FileInputStream(file), file.getName());
        super.setHeadersRows(afterLine);
        super.setLastsheet(lastSheet);
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine, boolean lastSheet, Class<?> group) throws IOException {
        super(clazz, new FileInputStream(file), file.getName());
        super.setHeadersRows(afterLine);
        super.setLastsheet(lastSheet);
        super.setGroup(group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, FileType type) {
        super(clazz, inputStream, type);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, FileType type, Class<?> group) {
        this(clazz, inputStream, type, Importer.DEFAULT_START_ROW, false, group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, FileType type, int afterLine) {
        this(clazz, inputStream, type, afterLine, false, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, FileType type, int afterLine, boolean isLastSheet) {
        this(clazz, inputStream, type, afterLine, isLastSheet, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, FileType type, int afterLine, boolean isLastSheet, Class<?> group) {
        this(clazz, inputStream, group, type);
        super.setLastsheet(isLastSheet);
        setHeadersRows(afterLine);
    }

    private ExcelParser(Class<T> clazz, InputStream inputStream, Class<?> group, FileType fileType) {
        super(clazz, inputStream, fileType);
        super.setGroup(group);
    }


    public void setAfterLine(int afterLine) {
        setHeadersRows(afterLine);
    }

}
