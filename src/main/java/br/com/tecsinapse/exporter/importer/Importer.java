package br.com.tecsinapse.exporter.importer;

import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Multimaps.filterEntries;
import static com.google.common.collect.Multimaps.transformValues;

import br.com.tecsinapse.exporter.FileType;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.annotation.TableCellMappings;
import br.com.tecsinapse.exporter.converter.group.Default;

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

    public List<T> parse() throws Exception {
        return parser.parse();
    }
}
