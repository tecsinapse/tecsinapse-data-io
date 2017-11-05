/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.tecsinapse.exporter.ExporterFormatter;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.TableCell;

public class ExportHtml {

    private final Charset charset;

    private final Map<String, String> tableHtmlProperties = new LinkedHashMap<>();

    public static ExportHtml newInstance(Charset charset) {
        return new ExportHtml(charset);
    }

    public ExportHtml(Charset charset) {
        this.charset = charset;
    }

    public static ExportHtml newInstance(Map<String, String> tableHtmlProperties, Charset charset) {
        ExportHtml exportHtml = new ExportHtml(charset);
        exportHtml.getTableHtmlProperties().putAll(tableHtmlProperties);
        return exportHtml;
    }

    public String toHtml(Table table) throws IOException {
        return toHtml(table, charset);
    }

    public String toHtml(Table table, Charset charset) throws IOException {
        checkNpe(table);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        writeToHtml(table, arrayOutputStream);
        return new String(arrayOutputStream.toByteArray(), charset);
    }

    public void toHtml(Table table, File file) throws IOException, NullPointerException {
        checkNpe(table);
        if (!file.exists() && file.createNewFile()) {
            throw new IOException("File not exists or not created!");
        }
        FileOutputStream fos = new FileOutputStream(file);
        writeToHtml(table, fos);
        fos.close();
    }

    public void writeToHtml(Table table, OutputStream outputStream) throws IOException {
        checkNpe(table);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, charset));
        bw.append("<table");
        for (Entry<String, String> prop : tableHtmlProperties.entrySet()) {
            writeNonNullProperty(bw, prop.getKey(), prop.getValue());
        }
        bw.append(">");
        bw.newLine();
        ExporterFormatter tableExporterFormatter = table.getExporterFormatter();
        for (List<TableCell> cells : table.getCells()) {
            bw.append("<tr>");
            bw.newLine();
            for (TableCell cell : cells) {
                bw.append("<td");
                writeNonNullProperty(bw, "class", cell.getStyleClass());
                writeNonNullPropertyIgnoreOne(bw, "rowspan", cell.getRowspan());
                writeNonNullPropertyIgnoreOne(bw, "colspan", cell.getColspan());
                writeNonNullProperty(bw, "style", cell.getStyle());
                bw.append(">");
                bw.append(cell.getFormattedContentInternalFirst(tableExporterFormatter));
                bw.append("</td>");
                bw.newLine();
            }
            bw.append("</tr>");
            bw.newLine();
        }
        bw.append("</table>");
        bw.newLine();
        bw.flush();
    }

    private void writeNonNullProperty(BufferedWriter bw, String property, Object s) throws IOException {
        if (s != null) {
            bw.append(String.format(" %s=\"%s\"", property, s));
        }
    }

    private void writeNonNullPropertyIgnoreOne(BufferedWriter bw, String property, Object s) throws IOException {
        if (s != null && !s.toString().isEmpty() && !"1".equals(s.toString())) {
            bw.append(String.format(" %s=\"%s\"", property, s));
        }
    }

    public Map<String, String> getTableHtmlProperties() {
        return tableHtmlProperties;
    }

    private <TP> void checkNpe(TP o) {
        if (o == null) {
            throw new NullPointerException("Object is null.");
        }
    }
}
