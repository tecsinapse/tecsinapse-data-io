package br.com.tecsinapse.exporter.importer;

import static br.com.tecsinapse.exporter.importer.ImporterXLSXType.DEFAULT;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Multimaps.filterEntries;
import static com.google.common.collect.Multimaps.transformValues;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.reflections.ReflectionUtils;

import br.com.tecsinapse.exporter.FileType;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.annotation.TableCellMappings;
import br.com.tecsinapse.exporter.converter.group.Default;

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
    private String dateStringPattern;
    private final Class<?> group;

    public Importer(Class<T> clazz, Charset charset, File file) throws IOException {
        this(clazz, charset, Default.class);
        this.file = file;
    }

    public Importer(Class<T> clazz, Charset charset, InputStream inputStream, String filename) throws IOException {
        this(clazz, inputStream, filename);
        this.charset = charset;
    }

    public Importer(Class<T> clazz, File file, ImporterXLSXType importerXLSXType) throws IOException {
    	this(clazz, file);
        this.importerXLSXType = importerXLSXType;
    }

    public Importer(Class<T> clazz, File file) throws IOException {
        this(clazz, Default.class);
        this.file = file;
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename) throws IOException {
        this(clazz, inputStream, filename, Default.class);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, Class<?> group) throws IOException {
        this(clazz, inputStream, filename, false, group);
    }

    public Importer(Class<T> clazz, InputStream inputStream, String filename, ImporterXLSXType importerXLSXType) throws IOException {
        this(clazz, inputStream, filename, false);
        this.importerXLSXType = importerXLSXType;
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
        this.afterLine = afterLine;
        this.isLastSheet = isLastSheet;
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
        if (dateStringPattern != null) {
            parser.setDateStringPattern(dateStringPattern);
        }
    }

	private void doBeforeParser() throws IOException {
        FileType fileType = getFileType();
        if(fileType == FileType.XLSX || fileType == FileType.XLS) {
            if(file != null) {
                parser = new ExcelParser<T>(clazz, file, afterLine, isLastSheet, importerXLSXType, group);
                return;
            }
            if(inputStream != null) {
                parser = new ExcelParser<T>(clazz, inputStream, fileType.getExcelType(), afterLine, isLastSheet, importerXLSXType, group);
                return;
            }
        }
        if(file != null) {
            parser = new CsvParser<T>(clazz, file, charset, afterLine, group);
            return;
        }
        if(inputStream != null) {
            parser = new CsvParser<T>(clazz, inputStream, charset, afterLine, group);
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

    //FIXME Refatorar: deixar método comum para ExcelParser e CsvParser
    protected static final Map<Method, TableCellMapping> getMappedMethods(Class<?> clazz, final Class<?> group) {

        Set<Method> cellMappingMethods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(TableCellMapping.class));
        cellMappingMethods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(TableCellMappings.class)));


        Multimap<Method, Optional<TableCellMapping>> tableCellMappingByMethod = FluentIterable.from(cellMappingMethods)
                .index(new Function<Method, Optional<TableCellMapping>>() {
                    @Override
                    public Optional<TableCellMapping> apply(Method method) {
                        return Optional.fromNullable(method.getAnnotation(TableCellMapping.class))
                                .or(getFirstTableCellMapping(method.getAnnotation(TableCellMappings.class), group));
                    }})
                .inverse();

        tableCellMappingByMethod = filterEntries(tableCellMappingByMethod, new Predicate<Entry<Method, Optional<TableCellMapping>>>() {
            @Override
            public boolean apply(Entry<Method, Optional<TableCellMapping>> entry) {
                return entry.getValue().isPresent()
                        && any(Lists.newArrayList(entry.getValue().get().groups()), assignableTo(group));
            }
        });

        Multimap<Method, TableCellMapping> methodByTableCellMapping = transformValues(tableCellMappingByMethod, new Function<Optional<TableCellMapping>, TableCellMapping>() {
            @Override
            public TableCellMapping apply(Optional<TableCellMapping> tcmOptional) {
                return tcmOptional.get();
            }});

        return Maps.transformValues(methodByTableCellMapping.asMap(), new Function<Collection<TableCellMapping>, TableCellMapping>() {
            @Override
            public TableCellMapping apply(Collection<TableCellMapping> tcms) {
                return Iterables.getFirst(tcms, null);
            }
        });
    }

    private static Optional<TableCellMapping> getFirstTableCellMapping(TableCellMappings tcms, final Class<?> group) {
        if (tcms == null) {
            return Optional.absent();
        }

        return FluentIterable.from(Lists.newArrayList(tcms.value()))
                .filter(new Predicate<TableCellMapping>() {
                    @Override
                    public boolean apply(TableCellMapping tcm) {
                        return any(Lists.newArrayList(tcm.groups()), assignableTo(group));
                    }})
                .first();
    }

    private static Predicate<? super Class<?>> assignableTo(final Class<?> group) {
        return new Predicate<Class<?>>() {
            @Override
            public boolean apply(Class<?> g) {
                return g.isAssignableFrom(group);
            }};
    }

    public void setAfterLine(int afterLine) {
        this.afterLine = afterLine;
    }

    public void setDateStringPattern(String dateStringPattern) {
        this.dateStringPattern = dateStringPattern;
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
