package br.com.tecsinapse.exporter.importer;

import br.com.tecsinapse.exporter.CSVUtil;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
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
		 * Percorre a linha em busca de ; 
		 * depois verifica se entre 2 ; existem aspas
		 * Se houver, é preciso ignorar os ; internos às aspas
		 */
		while (lastIndex != -1 && lastIndex < line.length()) {
			index = line.indexOf(";", lastIndex);

			if (index == -1) {
				//ultima coluna
				linhaParseadaPorAspas.add(line.substring(lastIndex).replace(";", ""));
				break;
			} else {
				String coluna = line.substring(lastIndex, index + 1);
				 
				if (temAspas(coluna)) {
					index = getFinalColuna(line.substring(lastIndex), lastIndex);
					if (index == -1) {
						//ultima coluna
						linhaParseadaPorAspas.add(line.substring(lastIndex).replace("\"\"", "\"").trim());
						break;
					}
					coluna = substringNormalizada(line, lastIndex, index - 1); 
					linhaParseadaPorAspas.add(coluna);
					lastIndex = index;
				} else {
					linhaParseadaPorAspas.add(coluna.replace(";", ""));
					lastIndex = index == -1 ? -1 : index + 1;
				}
			}
		}

		return linhaParseadaPorAspas;
	}

	private int getFinalColuna(String substring, int inicio) {
		char[] chars = substring.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\"') {
				for (int j = i + 1; j < chars.length; j++) {
					if (chars[j] == '\"') {
						return getFinalColuna(substring.substring(j + 1), inicio + j + 1);
					}
				}
			}
			
			if (chars[i] == ';') {
				return i + inicio + 1;
			}
		}
		
		return -1;
	}

	private boolean temAspas(String column) {
		return column.indexOf("\"") != -1;
	}
	
	private String substringNormalizada(String line, int i, int f) {
		line = line.substring(i, f - 1).trim();
		if (line.startsWith("\"")) {
			line = line.substring(1);
		}
		if (line.endsWith("\"")) {
			line = line.substring(0, line.length() - 1);
		}
		
		return line.replace("\"\"", "\"").trim();
	}
}
