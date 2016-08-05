/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import com.google.common.base.Strings;

import br.com.tecsinapse.exporter.Table;

public class SpreadsheetUtil {

    private static HttpServletResponse getResponseForExcel(String filenameWithExtension, FacesContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename=" + filenameWithExtension);
        return response;
    }

    private static HttpServletResponse getResponseForCsv(String filename, FacesContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".csv");
        return response;
    }

    private static HttpServletResponse getResponseForTxt(String filename, FacesContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType("text/plain");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".txt");
        return response;
    }

    private static void doExport(String name, String extension, Workbook wb) throws IOException {
        String filename = name + "_" + LocalDateTime.now().toString("dd-MM-yyyy_HH-mm");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = getResponseForExcel(filename + extension, context);
        wb.write(response.getOutputStream());
        context.responseComplete();
    }

    public static void exportXls(String name, Table t) throws IOException {
        Workbook wb = t.toHSSFWorkBook();
        doExport(name, ".xls", wb);
    }

    /**
     * Prefira {@code static void exportSXlsx(String name, Table t)} por fazer cache em disco e ser mais otimizado
     *
     * @param name nome do arquivo a ser gerado
     * @param t    {@link Table} a ser exportada
     * @throws IOException em caso de algum problema de {@code I/O}
     */
    public static void exportXlsx(String name, Table t) throws IOException {
        Workbook wb = t.toXSSFWorkBook();
        doExport(name, ".xlsx", wb);
    }

    public static void exportSXlsx(String name, Table t) throws IOException {
        Workbook wb = t.toWorkBook(new SXSSFWorkbook());
        doExport(name, ".xlsx", wb);
    }

    public static void exportCsvZip(String name, Table t, String charsetName) throws IOException {
        String filename = name + "_";
        filename += new DateTime(new Date()).toString("dd-MM-yyyy_HH-mm");

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            exportCsv(t, charsetName, baos);

            ZIPUtil.exportZip(filename, "csv", baos);
        }
    }

    public static void exportCsv(String name, Table t, String chartsetName) throws IOException {
        String filename = name + "_";
        filename += new DateTime(new Date()).toString("dd-MM-yyyy_HH-mm");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = getResponseForCsv(filename, context);

        exportCsv(t, chartsetName, response.getOutputStream());

        context.responseComplete();
    }

    public static void exportTxt(String name, Table t, String chartsetName) throws IOException {
        String filename = name + "_";
        filename += new DateTime(new Date()).toString("dd-MM-yyyy_HH-mm");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = getResponseForTxt(filename, context);

        exportCsv(t, chartsetName, response.getOutputStream());

        context.responseComplete();
    }

    public static void exportCsv(Table t, String chartsetName, OutputStream out) throws IOException {
        List<List<String>> csv = t.toStringMatrix();
        CsvUtil.write(csv, out, chartsetName);
    }

    public static File getCsvFile(Table t, String fileName, String charsetName) throws IOException {
        return getCsvFile(t, createFile(fileName), charsetName);
    }

    public static File getCsvFile(Table t, File f, String charsetName) throws IOException {
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
            CsvUtil.write(t.toStringMatrix(), fos, charsetName);
        }
        return f;
    }

    public static File getSvFile(Table t, String file, String charsetName, char separator) throws IOException {
        File f = createFile(file);
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
            CsvUtil.write(t.toStringMatrix(), fos, charsetName, separator);
        }
        return f;
    }

    public static File getXlsFile(Table t, String file) throws IOException {
        File f = createFile(file);
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
            t.toHSSFWorkBook().write(fos);
        }
        return f;
    }

    public static File getXlsxFile(Table t, String file) throws IOException {
        File f = createFile(file);
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
            t.toXSSFWorkBook().write(fos);
        }
        return f;
    }

    public static int getColumnIndexByColumnName(String columnName) {
        columnName = getColumnFromCellName(columnName.toUpperCase());
        int value = 0;
        for (int i = 0; i < columnName.length(); i++) {
            int delta = (columnName.charAt(i)) - 64;
            value = value * 26 + delta;
        }
        return value - 1;
    }

    private static String getColumnFromCellName(String cellReference) {
        if (Strings.isNullOrEmpty(cellReference)) {
            return "";
        }

        return cellReference.split("[0-9]*$")[0];
    }

    public static String getColumnNameByColumnIndex(int columnIndex) {
        String s = "";
        do {
            s = (char) ('A' + (columnIndex % 26)) + s;
            columnIndex /= 26;
        } while (columnIndex-- > 0);
        return s;
    }

    private static File createFile(String fileName) throws IOException {
        String ext = com.google.common.io.Files.getFileExtension(fileName);
        String name = com.google.common.io.Files.getNameWithoutExtension(fileName);
        return Files.createTempFile(name, ext.isEmpty() ? "" : "." + ext).toFile();
    }

}
