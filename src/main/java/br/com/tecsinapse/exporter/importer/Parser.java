package br.com.tecsinapse.exporter.importer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Parser<T> {
    List<T> parse() throws IllegalAccessException, InstantiationException, InvocationTargetException, Exception;
}
