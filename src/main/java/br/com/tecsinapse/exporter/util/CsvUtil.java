/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Strings;

import br.com.tecsinapse.exporter.type.SeparatorType;

public class CsvUtil {

    private static final char DEFAULT_SEPARATOR_CHAR = SeparatorType.SEMICOLON.getSeparator();

    private static final String SEPARATOR = String.valueOf(DEFAULT_SEPARATOR_CHAR);

    public static List<String> processInputCSV(InputStream inputStream, Charset charset) throws IOException {
        return processInputCSV(inputStream, true, charset);
    }

    public static List<String> processCSV(InputStream inputStream, Charset charset) throws IOException {
        return processInputCSV(inputStream, false, charset);
    }

    private static List<String> processInputCSV(InputStream inputStream, boolean ignoreFirstLine, Charset charset) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            if (ignoreFirstLine) {
                line = br.readLine();
            }

            while ((line = br.readLine()) != null) {

                if (!line.isEmpty() && line.split(SEPARATOR).length > 0) {
                    lines.add(line);
                }
            }
        }

        return lines;
    }

    public static String getColumnValue(String[] columns, int index) {
        if (index >= columns.length || Strings.isNullOrEmpty(columns[index])) {
            return null;
        }

        return columns[index].trim();
    }

    public static List<String> simpleLine(Object[] fields) throws Exception {
        List<String> list = new ArrayList<>();
        if (fields != null && fields.length > 0) {
            for (Object o : fields) {
                if (o == null) {
                    list.add("");
                } else {
                    list.add(o.toString());
                }
            }
        }
        return list;
    }

    public static <T> File writeCsv(List<List<T>> csv, String fileName, String chartsetName) throws IOException {
        File file = new File(fileName + ".csv");
        try (OutputStream o = new FileOutputStream(file)) {
            write(csv, o, chartsetName);
        }
        return file;
    }

    public static <T> void write(List<List<T>> csv, OutputStream o, String chartsetName, char separator) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(o, chartsetName)) {
            for (List<T> row : csv) {
                StringBuilder line = new StringBuilder();
                for (Iterator<T> iter = row.iterator(); iter.hasNext(); ) {
                    final T next = iter.next();
                    String field = "";
                    if (next != null) {
                        field = String.valueOf(next).replace("\"", "\"\"");
                        if (field.indexOf(separator) > -1 || field.indexOf('"') > -1) {
                            field = '"' + field + '"';
                        }
                    }
                    line.append(field);
                    if (iter.hasNext()) {
                        line.append(separator);
                    }
                }
                line.append("\n");
                writer.write(line.toString());
            }
            writer.flush();
        }
    }

    public static <T> void write(List<List<T>> csv, OutputStream o, String chartsetName) throws IOException {
        write(csv, o, chartsetName, DEFAULT_SEPARATOR_CHAR);
    }

}