package br.com.tecsinapse.exporter.importer;

import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Parser<T> extends Closeable {
    List<T> parse() throws IllegalAccessException, InstantiationException, InvocationTargetException, Exception;
}
