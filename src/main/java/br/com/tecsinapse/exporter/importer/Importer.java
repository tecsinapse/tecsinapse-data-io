/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.base.Strings;

import br.com.tecsinapse.exporter.converter.group.Default;
import br.com.tecsinapse.exporter.importer.parser.CsvParser;
import br.com.tecsinapse.exporter.importer.parser.SpreadsheetParser;
import br.com.tecsinapse.exporter.type.FileType;

public class Importer<T> implements Closeable {

    public static final int DEFAULT_START_ROW = 1;
    private final Class<?> group;
    private Class<T> clazz;
    private File file;
    private InputStream inputStream;
    private String filename;
    private Charset charset;
    private Parser<T> parser;
    private FileType fileType;
    private int headersRows = DEFAULT_START_ROW;

    public Importer(Class<T> clazz, Charset charset, File file) throws IOException {
        this(clazz, charset, Default.class);
        this.file = file;
    }

    public Importer(Class<T> clazz, Charset charset, InputStream inputStream, String filename) throws IOException {
        this(clazz, inputStream, filename);
        this.charset = charset;
    }

    public Importer(Class<T> clazz, File file) throws IOException {
        this(clazz, file, Default.class);
    }

    public Importer(Class<T> clazz, File file, Class<?> group) throws IOException {
        this(clazz, group);
        this.file = file;
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename) throws IOException {
        this(clazz, inputStream, filename, Default.class);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, Class<?> group) throws IOException {
        this(clazz, inputStream, filename, false, group);
    }

    @Deprecated
    public Importer(Class<T> clazz, InputStream inputStream, String filename, ImporterXLSXType importerXLSXType) throws IOException {
        this(clazz, inputStream, filename, false, Default.class);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, boolean isLastSheet) throws IOException {
        this(clazz, inputStream, filename, isLastSheet, Default.class);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, boolean isLastSheet, Class<?> group) throws IOException {
        this(clazz, inputStream, filename, DEFAULT_START_ROW, isLastSheet, group);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, int afterLine, boolean isLastSheet) throws IOException {
        this(clazz, inputStream, filename, afterLine, isLastSheet, Default.class);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, int afterLine, boolean isLastSheet, Class<?> group) throws IOException {
        this(clazz, group);
        this.inputStream = inputStream;
        this.filename = filename;
        this.headersRows = afterLine;
        //this.isLastSheet = isLastSheet;
    }

    private Importer(Class<T> clazz, Class<?> group) {
        this(clazz, Charset.defaultCharset(), group);
    }

    private Importer(Class<T> clazz, Charset charset, Class<?> group) {
        this.clazz = clazz;
        this.charset = charset;
        this.group = group;
    }

    private void beforeParser() throws IOException {
        doBeforeParser();
    }

    private void doBeforeParser() throws IOException {
        FileType fileType = getFileType();
        if (fileType == FileType.XLSX || fileType == FileType.XLS) {
            if (file != null) {
                parser = new SpreadsheetParser<T>(clazz, file, group);
                // TODO Last Row?
                parser.setHeadersRows(headersRows);
                return;
            }
            if (inputStream != null) {
                parser = new SpreadsheetParser<T>(clazz, inputStream, group, fileType);
                // TODO Last Row?
                parser.setHeadersRows(headersRows);
                return;
            }
        }
        if (file != null) {
            parser = new CsvParser<T>(clazz, file, charset, headersRows, group);
            return;
        }
        if (inputStream != null) {
            parser = new CsvParser<T>(clazz, inputStream, charset, headersRows, group);
            return;
        }
    }

    public FileType getFileType() {
        if (this.file == null && Strings.isNullOrEmpty(this.filename)) {
            throw new IllegalStateException("File is null and filename is null");
        }
        String name = this.filename;
        if (file != null) {
            name = file.getName();
        }
        return FileType.getFileType(name);
    }

    public void setAfterLine(int afterLine) {
        this.headersRows = afterLine;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public List<T> parse() throws Exception {
        beforeParser();
        return parser.parse();
    }

    public int getNumberOfSheets() {
        return parser.getNumberOfSheets();
    }

    @Override
    public void close() throws IOException {
        parser.close();
    }
}
