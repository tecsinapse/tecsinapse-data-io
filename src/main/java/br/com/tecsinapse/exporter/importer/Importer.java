package br.com.tecsinapse.exporter.importer;

import static br.com.tecsinapse.exporter.importer.ImporterXLSXType.DEFAULT;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.base.Strings;

import br.com.tecsinapse.exporter.FileType;

public class Importer<T> implements Closeable {

    static final int DEFAULT_START_ROW = 1;

    private Class<T> clazz;
    private File file;
    private InputStream inputStream;
    private String filename;
    private Charset charset;
    private Parser<T> parser;

    private int afterLine = DEFAULT_START_ROW;
    private boolean isLastSheet;
    private ImporterXLSXType importerXLSXType = DEFAULT;

    private Importer(Class<T> clazz, Charset charset) {
        this.clazz = clazz;
        this.charset = charset;
    }

    private Importer(Class<T> clazz) {
    	this(clazz, Charset.defaultCharset());
    }

    public Importer(Class<T> clazz, Charset charset, File file) throws IOException {
        this(clazz, charset);
        this.file = file;
    }

    public Importer(Class<T> clazz, Charset charset, InputStream inputStream, String filename) throws IOException {
        this(clazz, inputStream, filename);
        this.charset = charset;
    }

    public Importer(Class<T> clazz, File file) throws IOException {
        this(clazz);
        this.file = file;
    }
    
    public Importer(Class<T> clazz, File file, ImporterXLSXType importerXLSXType) throws IOException {
    	this(clazz, file);
        this.importerXLSXType = importerXLSXType;
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename) throws IOException {
        this(clazz, inputStream, filename, false);
    }
    
    public Importer(Class<T> clazz, InputStream inputStream, String filename, ImporterXLSXType importerXLSXType) throws IOException {
        this(clazz, inputStream, filename, false);
        this.importerXLSXType = importerXLSXType;
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, boolean isLastSheet) throws IOException {
        this(clazz, inputStream, filename, DEFAULT_START_ROW, isLastSheet);
    }
    
    public Importer(Class<T> clazz, InputStream inputStream, String filename, int afterLine, boolean isLastSheet) throws IOException {
        this(clazz);
        this.inputStream = inputStream;
        this.filename = filename;
        this.afterLine = afterLine;
        this.isLastSheet = isLastSheet;
    }

	private void beforeParser() throws IOException {
        FileType fileType = getFileType();
        if(fileType == FileType.XLSX || fileType == FileType.XLS) {
            if(file != null) {
                parser = new ExcelParser<T>(clazz, file, afterLine, isLastSheet, importerXLSXType);
                return;
            }
            if(inputStream != null) {
                parser = new ExcelParser<T>(clazz, inputStream, fileType.getExcelType(), afterLine, isLastSheet, importerXLSXType);
                return;
            }
        }
        if(file != null) {
            parser = new CsvParser<T>(clazz, file, charset, afterLine);
            return;
        }
        if(inputStream != null) {
            parser = new CsvParser<T>(clazz, inputStream, charset);
            return;
        }

    }

    public FileType getFileType() {
        if(this.file == null && Strings.isNullOrEmpty(this.filename)) {
            throw new IllegalStateException("File is null and filename is null");
        }
        String name = this.filename;
        if(file != null) {
            name = file.getName();
        }
        return FileType.getFileType(name);
    }

    public void setAfterLine(int afterLine) {
        this.afterLine = afterLine;
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
