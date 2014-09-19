package br.com.tecsinapse.exporter.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;

import br.com.tecsinapse.exporter.FixedLengthFileUtil;
import br.com.tecsinapse.exporter.annotation.FixedLengthColumn;
import br.com.tecsinapse.exporter.converter.TableCellConverter;

import com.google.common.primitives.Ints;

public class FixedLengthFileParser<T> {

    private final Class<T> clazz;

    private Charset charset = Charset.defaultCharset();
    private boolean ignoreFirstLine = false;
    private boolean ignoreMalformedLines = false;

    public FixedLengthFileParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public FixedLengthFileParser<T> withCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public FixedLengthFileParser<T> withIgnoreFirstLine(boolean ignoreFirstLine) {
        this.ignoreFirstLine = ignoreFirstLine;
        return this;
    }

    public FixedLengthFileParser<T> withIgnoreMalformedLines(boolean ignoreMalformedLines) {
        this.ignoreMalformedLines = ignoreMalformedLines;
        return this;
    }

    public List<T> parse(File file) throws IOException, ReflectiveOperationException {
        return parse(new FileInputStream(file));
    }

    public List<T> parse(InputStream inputStream) throws IOException, ReflectiveOperationException {

        final List<T> list = new ArrayList<>();
        final List<String> lines = FixedLengthFileUtil.getLines(inputStream, ignoreFirstLine, charset);

        @SuppressWarnings("unchecked")
        final Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
                ReflectionUtils.<Method> withAnnotation(FixedLengthColumn.class));

        final List<AnnotationMethod> methodsAndAnnotations = orderedAnnotationsAndMethods(methods);

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        for (int i = 0; i < lines.size(); i++) {
            T instance = constructor.newInstance();
            String workingLine = lines.get(i);
            for (AnnotationMethod annotationMethod : methodsAndAnnotations) {
                Method method = annotationMethod.getMethod();
                FixedLengthColumn flc = annotationMethod.getFlc();
                if (workingLine.length() < flc.columnSize()) {
                    if (ignoreMalformedLines) {
                        break;
                    } else {
                        int line = ignoreFirstLine ? i + 1 : i;
                        throw new IllegalStateException(String.format(
                                "Malformed file or wrong configuration. Line %s size doesn't match the configuration.",
                                line));
                    }
                }
                String value = workingLine.substring(0, flc.columnSize()).trim();
                TableCellConverter<?> converter = flc.converter().newInstance();
                Object obj = converter.apply(value);
                method.invoke(instance, obj);
                workingLine = workingLine.substring(flc.columnSize(), workingLine.length());
            }
            list.add(instance);
        }
        return list;
    }

    private List<AnnotationMethod> orderedAnnotationsAndMethods(final Set<Method> methods) {
        final List<AnnotationMethod> methodsAndAnnotations = new ArrayList<>();
        for (Method method : methods) {
            FixedLengthColumn flc = method.getAnnotation(FixedLengthColumn.class);
            if (flc == null) {
                continue;
            }
            AnnotationMethod annotationMethod = new AnnotationMethod(method, flc);
            methodsAndAnnotations.add(annotationMethod);
        }
        Collections.sort(methodsAndAnnotations);

        return methodsAndAnnotations;
    }

    /*
     * private String getValueOrEmpty(List<String> fields, int index) { if
     * (fields.isEmpty() || fields.size() <= index) { return ""; } return
     * fields.get(index); }
     * 
     * private List<String> split(String line) { int index = 0; int lastIndex =
     * 0;
     * 
     * List<String> linhaParseadaPorAspas = new ArrayList<>();
     * 
     * while (lastIndex != -1 && lastIndex < line.length()) { index =
     * line.indexOf(";", lastIndex);
     * 
     * if (index == -1) { // ultima coluna
     * linhaParseadaPorAspas.add(line.substring(lastIndex).replace(";", ""));
     * break; } else { String coluna = line.substring(lastIndex, index + 1);
     * 
     * if (temAspas(coluna)) { index = getFinalColuna(line.substring(lastIndex),
     * lastIndex); if (index == -1) { // ultima coluna
     * linhaParseadaPorAspas.add(line.substring(lastIndex).replace("\"\"",
     * "\"").trim()); break; } coluna = substringNormalizada(line, lastIndex,
     * index - 1); linhaParseadaPorAspas.add(coluna); lastIndex = index; } else
     * { linhaParseadaPorAspas.add(coluna.replace(";", "")); lastIndex = index
     * == -1 ? -1 : index + 1; } } }
     * 
     * return linhaParseadaPorAspas; }
     * 
     * private int getFinalColuna(String substring, int inicio) { char[] chars =
     * substring.toCharArray();
     * 
     * for (int i = 0; i < chars.length; i++) { if (chars[i] == '\"') { for (int
     * j = i + 1; j < chars.length; j++) { if (chars[j] == '\"') { return
     * getFinalColuna(substring.substring(j + 1), inicio + j + 1); } } }
     * 
     * if (chars[i] == ';') { return i + inicio + 1; } }
     * 
     * return -1; }
     * 
     * private boolean temAspas(String column) { return column.indexOf("\"") !=
     * -1; }
     * 
     * private String substringNormalizada(String line, int i, int f) { line =
     * line.substring(i, f - 1).trim(); if (line.startsWith("\"")) { line =
     * line.substring(1); } if (line.endsWith("\"")) { line = line.substring(0,
     * line.length() - 1); }
     * 
     * return line.replace("\"\"", "\"").trim(); }
     */

    private static class AnnotationMethod implements Comparable<AnnotationMethod> {
        private final Method method;
        private final FixedLengthColumn flc;

        public AnnotationMethod(Method method, FixedLengthColumn flc) {
            this.method = method;
            this.flc = flc;
        }

        public Method getMethod() {
            return method;
        }

        public FixedLengthColumn getFlc() {
            return flc;
        }

        @Override
        public int compareTo(AnnotationMethod other) {
            return Ints.compare(this.flc.columnIndex(), other.getFlc().columnIndex());
        }

    }
}
