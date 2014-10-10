package br.com.tecsinapse.exporter.importer;

import br.com.tecsinapse.exporter.FileType;
import br.com.tecsinapse.exporter.converter.group.Default;

import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class Importer<T> {

    public static final int DEFAULT_START_ROW = 1;

    private Class<T> clazz;
    private File file;
    private InputStream inputStream;
    private String filename;
    private Charset charset;
    private Parser<T> parser;
    private int initialRow;
    private Class<?> group = Default.class;

    private Importer(Class<T> clazz) {
        this(clazz, Charset.defaultCharset());
    }

    private Importer(Class<T> clazz, Charset charset) {
        this.clazz = clazz;
        this.charset = charset;
    }

    public Importer(Class<T> clazz, File file) throws IOException {
        this(clazz, file, Default.class);
    }

    public Importer(Class<T> clazz, File file, Class<?> group) throws IOException {
        this(clazz);
        this.file = file;
        this.group = group;
        prepareParser();
    }

    public Importer(Class<T> clazz, Charset charset, File file) throws IOException {
        this(clazz, charset);
        this.file = file;
        prepareParser();
    }

    public Importer(Class<T> clazz, Charset charset, InputStream inputStream, String filename) throws IOException {
        this(clazz, charset);
        this.inputStream = inputStream;
        this.filename = filename;
        prepareParser();
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename) throws IOException {
        this(clazz, inputStream, filename, DEFAULT_START_ROW, Default.class);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, Class<?> group) throws IOException {
        this(clazz, inputStream, filename, DEFAULT_START_ROW, group);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, int initialRow, Class<?> group) throws IOException {
        this(clazz);
        this.inputStream = inputStream;
        this.filename = filename;
        this.initialRow = initialRow;
        this.group = group;
        prepareParser();
    }

    private void prepareParser() throws IOException {
        FileType fileType = getFileType();
        if(fileType == FileType.XLSX || fileType == FileType.XLS) {
            if(file != null) {
                parser = new ExcelParser<T>(clazz, file, initialRow, group);
                return;
            }
            if(inputStream != null) {
                parser = new ExcelParser<T>(clazz, inputStream, fileType.getExcelType(), initialRow, group);
                return;
            }
        }
        if(file != null) {
            parser = new CsvParser<T>(clazz, file, charset, group);
            return;
        }
        if(inputStream != null) {
            parser = new CsvParser<T>(clazz, inputStream, charset, group);
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

    public List<T> parse() throws Exception {
        return parser.parse();
    }
}
