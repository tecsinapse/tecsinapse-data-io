/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;

public class ExportHtml {

    private final Map<String, String> tableHtmlProperties = new HashMap<>();

    public static ExportHtml newInstance() {
        return new ExportHtml();
    }

    public static ExportHtml newInstance(Map<String, String> tableHtmlProperties) {
        ExportHtml exportHtml = new ExportHtml();
        exportHtml.getTableHtmlProperties().putAll(tableHtmlProperties);
        return exportHtml;
    }

    public String toHtml(Table table) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        writeToHtml(table, arrayOutputStream);
        return new String(arrayOutputStream.toByteArray());
    }

    public void toHtml(Table table, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        writeToHtml(table, new FileOutputStream(file));
    }

    public void writeToHtml(Table table, OutputStream outputStream) {
        if (table == null) {
            return;
        }
        PrintStream printStream = new PrintStream(outputStream);
        printStream.append("<table");
        for (Entry<String, String> prop : tableHtmlProperties.entrySet()) {
            writeNonNullProperty(printStream, prop.getKey(), prop.getValue());
        }
        printStream.println(">");
        for (List<TableCell> cells : table.getCells()) {
            printStream.println("<tr>");
            for (TableCell cell : cells) {
                printStream.append("<td");
                writeNonNullProperty(printStream, "style", cell.getStyle());
                writeNonNullProperty(printStream, "class", cell.getStyleClass());
                writeNonNullProperty(printStream, "rowspan", cell.getRowspan());
                writeNonNullProperty(printStream, "colspan", cell.getColspan());
                printStream.append(">");
                printStream.append(cell.getContent());
                printStream.println("</td>");
            }
            printStream.println("</tr>");
        }
        printStream.println("</table>");
    }

    private void writeNonNullProperty(PrintStream printStream, String property, Object s) {
        if (s != null) {
            printStream.append(String.format(" %s=\"%s\"", property, s));
        }

    }

    public Map<String, String> getTableHtmlProperties() {
        return tableHtmlProperties;
    }
}
