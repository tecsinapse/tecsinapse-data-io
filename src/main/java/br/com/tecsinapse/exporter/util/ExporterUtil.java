/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.util;

import static br.com.tecsinapse.exporter.type.SeparatorType.SEMICOLON;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.exceptions.ExporterNotImplementedException;
import br.com.tecsinapse.exporter.txt.FieldTxt;
import br.com.tecsinapse.exporter.txt.FileTxt;
import br.com.tecsinapse.exporter.type.FileType;

public final class ExporterUtil {
    private ExporterUtil() {

    }

    public static File writeDataToFile(Table table, FileType fileType, String filename) throws IOException, ExporterNotImplementedException {
        return writeDataToFile(table, fileType, filename, "UTF-8");
    }

    public static File writeDataToFile(Table table, FileType fileType, String filename, String charset) throws IOException, ExporterNotImplementedException {
        return writeDataToFile(table, fileType, filename, charset, SEMICOLON.getSeparator());
    }

    public static File writeDataToFile(Table table, FileType fileType, String filename, String charset, char separator) throws IOException, ExporterNotImplementedException {
        File file = createFile(filename);
        writeData(table, fileType, new FileOutputStream(file), charset, separator);
        return file;
    }

    public static void writeData(Table table, FileType fileType, OutputStream outputStream, String charset, char separator) throws IOException, ExporterNotImplementedException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        if (fileType == FileType.XLSX || fileType == FileType.XLSM) {
            writeXlsx(table, bufferedOutputStream);
            return;
        }
        if (fileType == FileType.XLS) {
            writeXls(table, bufferedOutputStream);
            return;
        }
        if (fileType == FileType.CSV || fileType == FileType.TXT) {
            writeCsv(table, bufferedOutputStream, charset, separator);
            return;
        }
        throw new ExporterNotImplementedException(String.format("File type not supported. %s", fileType));
    }

    public static void writeFileTxtToOutput(FileTxt file, String chartsetName, OutputStream out) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(out, chartsetName)) {
            for (List<FieldTxt> lines : file.getFields()) {
                StringBuilder line = new StringBuilder();
                for (Iterator<FieldTxt> it = lines.iterator(); it.hasNext(); ) {
                    FieldTxt field = it.next();
                    line.append(field.getValue());
                    if (it.hasNext() || file.isEndsWithSeparator()) {
                        line.append(field.getSeparator());
                    }
                }
                line.append("\r\n");
                writer.write(line.toString());
            }
            writer.flush();
        }
    }

    private static void writeXlsx(Table table, OutputStream outputStream) throws IOException {
        table.toXSSFWorkBook().write(outputStream);
    }

    private static void writeXls(Table table, OutputStream outputStream) throws IOException {
        table.toHSSFWorkBook().write(outputStream);
    }

    private static void writeCsv(Table table, OutputStream outputStream, String charset, char separator) throws IOException {
        CsvUtil.write(table.toStringMatrix(), outputStream, charset, separator);
    }

    private static File createFile(String fileName) throws IOException {
        String ext = com.google.common.io.Files.getFileExtension(fileName);
        String name = com.google.common.io.Files.getNameWithoutExtension(fileName);
        return Files.createTempFile(name, ext.isEmpty() ? "" : "." + ext).toFile();
    }

    public static File getCsvFile(Table t, String fileName, String charsetName, char separator) throws IOException {
        return writeDataToFile(t, FileType.CSV, fileName, charsetName, separator);
    }

    public static File getCsvFile(Table t, File f, String charsetName, char separator) throws IOException {
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
            writeData(t, FileType.CSV, fos, charsetName, separator);
        }
        return f;
    }

    public static File getXlsFile(Table t, String file) throws IOException {
        return writeDataToFile(t, FileType.XLS, file);
    }

    public static File getXlsxFile(Table t, String file) throws IOException {
        return writeDataToFile(t, FileType.XLSX, file);
    }

    public static void writeCsvToOutput(Table t, String chartsetName, OutputStream out) throws IOException {
        writeData(t, FileType.CSV, out, chartsetName, SEMICOLON.getSeparator());
    }
}
