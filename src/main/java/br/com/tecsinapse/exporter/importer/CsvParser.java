package br.com.tecsinapse.exporter.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;

import br.com.tecsinapse.exporter.CSVUtil;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.TableCellConverter;

class CsvParser<T> implements Parser<T> {
	private final Class<T> clazz;
    private List<String> csvLines;

    private int afterLine = Importer.DEFAULT_START_ROW;

    private CsvParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    CsvParser(Class<T> clazz, File file, Charset charset, int afterLine) throws IOException {
        this(clazz, file, charset);

        this.afterLine = afterLine;
    }

    CsvParser(Class<T> clazz, InputStream input, Charset charset, int afterLine) throws IOException {
        this(clazz, input, charset);

        this.afterLine = afterLine;
    }

	public CsvParser(Class<T> clazz, List<String> csvLines) {
		this(clazz);
		this.csvLines = csvLines;
	}

	public CsvParser(Class<T> clazz, File file, Charset charset) throws
			IOException {
		this(clazz, CSVUtil.processCSV(new FileInputStream(file), charset));
	}

	public CsvParser(Class<T> clazz, InputStream inputStream, Charset charset)
			throws IOException {
		this(clazz, CSVUtil.processCSV(inputStream, charset));
	}

    @Override
    public int getNumberOfSheets() {
        return 1;
    }

    /**
	 * Não lê a primeira linha
	 * <p/>
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<T> parse() throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {
		List<T> list = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Set<Method> methods = ReflectionUtils.getAllMethods(clazz, 
				ReflectionUtils.<Method>withAnnotation(TableCellMapping.class));

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        for (int i = 0; i < csvLines.size(); i++) {
            final String line = csvLines.get(i);
            if ((i + 1) <= afterLine) {
                continue;
            }

            List<String> fields = split(line);
            T instance = constructor.newInstance();

			for (Method method : methods) {
                method.setAccessible(true);

				TableCellMapping tcm = method.getAnnotation(TableCellMapping.class);
				String value = getValueOrEmpty(fields, tcm.columnIndex());
				TableCellConverter<?> converter = tcm.converter().newInstance();
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

    @Override
    public void close() throws IOException {
        //nada parser é feito atualmente no construtor
    }
}
