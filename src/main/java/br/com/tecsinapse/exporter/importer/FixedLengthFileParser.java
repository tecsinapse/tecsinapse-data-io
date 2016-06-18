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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;

import br.com.tecsinapse.exporter.FixedLengthFileUtil;
import br.com.tecsinapse.exporter.annotation.FixedLengthColumn;
import br.com.tecsinapse.exporter.annotation.LineFixedLengthFile;
import br.com.tecsinapse.exporter.converter.TableCellConverter;

public class FixedLengthFileParser<T> {

    private final Class<T> clazz;

    private Charset charset = Charset.defaultCharset();
    private boolean ignoreFirstLine = false;
    private boolean ignoreLineWhenError = false;
    private boolean removeDuplicatedSpaces = true;
    private int afterLine = 0;
    private String eof = null;
    private int ignoreLastLines = 0;
    private boolean ignoreColumnLengthError = false;

    public FixedLengthFileParser(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    public FixedLengthFileParser<T> withAfterLine(int afterLine){
        this.afterLine = afterLine;
        return this;
    }

    public FixedLengthFileParser<T> withCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public FixedLengthFileParser<T> withIgnoreFirstLine(boolean ignoreFirstLine) {
        this.ignoreFirstLine = ignoreFirstLine;
        return this;
    }

    public FixedLengthFileParser<T> withIgnoreLineWhenErrors(boolean ignoreLineWhenError) {
        this.ignoreLineWhenError = ignoreLineWhenError;
        return this;
    }

    public FixedLengthFileParser<T> withRemoveDuplicatedSpaces(boolean removeDuplicatedSpaces) {
        this.removeDuplicatedSpaces = removeDuplicatedSpaces;
        return this;
    }
    
    public FixedLengthFileParser<T> withEofCharacter(String eof){
        this.eof = eof;
        return this;
    }

    public FixedLengthFileParser<T> withIgnoreLastLines(int linesToIgnore) {
        this.ignoreLastLines = linesToIgnore;
        return this;
    }

    public FixedLengthFileParser<T> withIgnoreColumnLengthError(boolean ignoreColumnLengthError) {
        this.ignoreColumnLengthError = ignoreColumnLengthError;
        return this;
    }

    public List<T> parse(File file) throws IOException, ReflectiveOperationException {
        return parse(new FileInputStream(file));
    }

    public List<T> parse(InputStream inputStream) throws IOException, ReflectiveOperationException {

        final List<T> list = new ArrayList<>();
        final List<String> lines = FixedLengthFileUtil.getLines(inputStream, ignoreFirstLine, afterLine, ignoreLastLines, eof, charset);

        @SuppressWarnings("unchecked")
        final Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
                ReflectionUtils.<Method> withAnnotation(FixedLengthColumn.class));
        
        @SuppressWarnings("unchecked")
        Method lineMethod = Iterables.getFirst(ReflectionUtils.getMethods(clazz, ReflectionUtils.<Method> withAnnotation(LineFixedLengthFile.class)), null);

        final List<AnnotationMethod> methodsAndAnnotations = orderedAnnotationsAndMethods(methods);

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        for (int i = 0; i < lines.size(); i++) {
            T instance = constructor.newInstance();
            String workingLine = lines.get(i);
            boolean ignoreLine = false;
            for (AnnotationMethod annotationMethod : methodsAndAnnotations) {
                if (workingLine.length() == 0) {
                    break;
                }
                Method method = annotationMethod.getMethod();
                FixedLengthColumn flc = annotationMethod.getFlc();
                final boolean naoValidouTamanhoDaLinha = !flc.useLineLength() && workingLine.length() < flc.columnSize() && !ignoreColumnLengthError;
                if (naoValidouTamanhoDaLinha) {
                    if (ignoreLineWhenError) {
                        ignoreLine = true;
                        break;
                    } else {
                        int line = ignoreFirstLine ? i + 1 : i;
                        throw new IllegalStateException(String.format(
                                "Malformed file or wrong configuration. Line %s size doesn't match the configuration.",
                                line));
                    }
                }
                int length = flc.useLineLength() || workingLine.length() < flc.columnSize() ? workingLine.length() : flc.columnSize();
                String value = workingLine.substring(0, length).trim();
                if (removeDuplicatedSpaces) {
                    value = value.replaceAll("\\s+", " ");
                }
                TableCellConverter<?> converter = flc.converter().newInstance();
                Object obj;
                try {
                    obj = converter.apply(value);
                } catch (Exception e) {
                    if (ignoreLineWhenError) {
                        ignoreLine = true;
                        break;
                    } else {
                        throw e;
                    }
                }
                method.invoke(instance, obj);
                workingLine = workingLine.substring(length, workingLine.length());
            }
            if(lineMethod != null){
                lineMethod.invoke(instance, lines.get(i));
            }
            if (!ignoreLine) {
                list.add(instance);
            }
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
