package br.com.tecsinapse.exporter.importer;

import br.com.tecsinapse.exporter.CSVUtil;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.reflections.ReflectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CsvImporter<T> {
    private final Class<T> clazz;
    private List<String> csvLines;

    public CsvImporter(Class<T> clazz, List<String> csvLines) {
        this(clazz);
        this.csvLines = csvLines;
    }

    public CsvImporter(Class<T> clazz, File file, Charset charset) throws IOException {
        this(clazz, CSVUtil.processInputCSV(new FileInputStream(file), charset));
    }

    public CsvImporter(Class<T> clazz, InputStream inputStream, Charset charset) throws IOException {
        this(clazz, CSVUtil.processInputCSV(inputStream, charset));
    }

    private CsvImporter(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Não lê a primeira linha
     * @return
     * @throws Exception
     */
    public List<T> parse() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> list = new ArrayList<>();
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(TableCellMapping.class));

        for (String line : csvLines) {
            List<String> fields = Lists.newArrayList(Splitter.on(";").split(line).iterator());
            T instance = clazz.newInstance();

            for (Method method : methods) {
                TableCellMapping tcm = method.getAnnotation(TableCellMapping.class);
                String value = getValueOrEmpty(fields, tcm.columnIndex());
                TableCellConverter converter = tcm.converter().newInstance();
                Object obj = converter.apply(value);
                method.invoke(instance, obj);
            }
            list.add(instance);
        }
        return list;
    }

    private String getValueOrEmpty(List<String> fields, int index) {
        if (fields.isEmpty() || fields.size() <= index) {
            return "";
        }
        return fields.get(index);
    }

}
