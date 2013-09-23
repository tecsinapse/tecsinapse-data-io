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

class CsvParser<T> implements Parser<T> {
	private final Class<T> clazz;
	private List<String> csvLines;

	public CsvParser(Class<T> clazz, List<String> csvLines) {
		this(clazz);
		this.csvLines = csvLines;
	}

	public CsvParser(Class<T> clazz, File file, Charset charset) throws
			IOException {
		this(clazz, CSVUtil.processInputCSV(new FileInputStream(file), charset));
	}

	public CsvParser(Class<T> clazz, InputStream inputStream, Charset charset)
			throws IOException {
		this(clazz, CSVUtil.processInputCSV(inputStream, charset));
	}

	private CsvParser(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Não lê a primeira linha
	 * <p/>
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<T> parse() throws IllegalAccessException, InstantiationException,
			InvocationTargetException {
		List<T> list = new ArrayList<>();
		Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils
				.withAnnotation(TableCellMapping.class));

		for (String line : csvLines) {
			List<String> fields = split(line);
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

	private List<String> split(String line) {
		int index = 0;
		int lastIndex = 0;

		List<String> linhaParseadaPorAspas = new ArrayList<>();

		/**
		 * Primeiro separa os campos com aspas, pois valores dentro de aspas devem
		 * ser tratados como um único campo. Ex: x;\"a;b;c\";z gera 3 colunas: [x]
		 * [a;b;c] [z]
		 */
		while (index != -1 && index < line.length() - 1) {
			index = line.indexOf("\"", lastIndex);

			if (index != -1) {
				String parteSemAspas = line.substring(lastIndex, index);

				if (parteSemAspas.length() > 1) {
					//tem parte sem aspas
					parteSemAspas = parteSemAspas.trim();
					if (parteSemAspas.startsWith(";")) {
						parteSemAspas = parteSemAspas.substring(1);
					}
					if (parteSemAspas.endsWith(";")) {
						parteSemAspas = parteSemAspas.substring(0, parteSemAspas
								.length() - 1);
					}

					linhaParseadaPorAspas.addAll(Lists.newArrayList(Splitter.on(';')
							.split(parteSemAspas)));
				}

				int index2 = line.indexOf("\"", index + 1);

				if (index2 == -1) {
					System.out.println(
							"Não foi encontrado o fechamento de aspas. "
							+ "As aspas serão tratadas como um caractere comum.");
					return Lists
							.newArrayList(Splitter.on(";").split(line).iterator());
				}

				String parteComAspas = line.substring(index + 1, index2);

				linhaParseadaPorAspas.add(parteComAspas.replace("\"", ""));

				index = index2;
				lastIndex = index2 + 1;
			} else {
				if (lastIndex < line.length() && lastIndex > 0) {
					linhaParseadaPorAspas.addAll(Lists.newArrayList(Splitter.on(";")
							.split(line.substring(lastIndex + 1)).iterator()));
					return linhaParseadaPorAspas;
				} else {
					return Lists
							.newArrayList(Splitter.on(";").split(line).iterator());
				}
			}
		}

		return linhaParseadaPorAspas;
	}
}
