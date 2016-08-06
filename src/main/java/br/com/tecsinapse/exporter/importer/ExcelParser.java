/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import br.com.tecsinapse.exporter.ExcelType;
import br.com.tecsinapse.exporter.converter.group.Default;
import br.com.tecsinapse.exporter.importer.parser.SpreadsheetParser;

/**
 * Does some thing in old style. It will be removed in version 2.0.0
 *
 * @deprecated use {@link br.com.tecsinapse.exporter.importer.parser.SpreadsheetParser}
 */
@Deprecated
public class ExcelParser<T> extends SpreadsheetParser<T> {

    public ExcelParser(Class<T> clazz, File file) throws IOException {
        this(clazz, new FileInputStream(file), ExcelType.getExcelType(file.getName()));
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine) throws IOException {
        this(clazz, new FileInputStream(file), ExcelType.getExcelType(file.getName()), afterLine);
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine, boolean lastSheet) throws IOException {
        this(clazz, file, afterLine, lastSheet, Default.class);
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine, boolean lastSheet, Class<?> group) throws IOException {
        this(clazz, new FileInputStream(file), ExcelType.getExcelType(file.getName()), afterLine, lastSheet, group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type) {
        this(clazz, inputStream, type, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, Class<?> group) {
        this(clazz, inputStream, type, Importer.DEFAULT_START_ROW, false, group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine) {
        this(clazz, inputStream, type, afterLine, false, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine, boolean isLastSheet) {
        this(clazz, inputStream, type, afterLine, isLastSheet, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine, boolean isLastSheet, ImporterXLSXType importerXLSXType) {
        this(clazz, inputStream, type, afterLine, isLastSheet);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine, boolean isLastSheet, Class<?> group) {
        this(clazz, inputStream, group, type.getFileType());
        if (isLastSheet) {
            setSheetNumber(getNumberOfSheets() - 1);
        }
        setHeadersRows(afterLine);
    }

    private ExcelParser(Class<T> clazz, InputStream inputStream, Class<?> group, br.com.tecsinapse.exporter.type.FileType fileType) {
        super(clazz, inputStream, "noname", fileType);
        super.setGroup(group);
    }


    public void setAfterLine(int afterLine) {
        setHeadersRows(afterLine);
    }

}
