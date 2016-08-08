/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.servlet;

import static br.com.tecsinapse.exporter.type.FileType.TXT;
import static br.com.tecsinapse.exporter.type.SeparatorType.SEMICOLON;
import static br.com.tecsinapse.exporter.util.Constants.DATE_TIME_FILE_NAME;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.txt.FileTxt;
import br.com.tecsinapse.exporter.type.FileType;
import br.com.tecsinapse.exporter.util.ExporterUtil;

public final class ExportServletUtil {

    private ExportServletUtil() {
    }

    public static FacesContext configureContentAndHeadersAndGetFacesContext(String filename, FileType fileType, Workbook wb) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        configureContentAndHeaders(filename, fileType, response);
        wb.write(response.getOutputStream());
        return context;
    }

    public static void configureContentAndHeaders(String filename, FileType fileType, HttpServletResponse response) {
        response.setContentType(fileType.getMimeType());
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
    }

    public static void facesDownloadResponse(Table table, String filename, FileType fileType, boolean zipFile) throws IOException {
        facesDownloadResponse(table, filename, fileType, "UTF-8", zipFile);
    }

    public static void facesDownloadResponse(Table table, String filename, FileType fileType, String charset, boolean zipFile) throws IOException {
        facesDownloadResponse(table, filename, fileType, charset, SEMICOLON.getSeparator(), zipFile);
    }

    public static void facesDownloadResponse(Table table, String filename, FileType fileType, String charset, char separator, boolean zipFile) throws IOException {
        if (zipFile) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ExporterUtil.writeData(table, fileType, baos, charset, separator);
            facesDownloadZip(baos, filename);
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        configureContentAndHeaders(filename, fileType, response);
        ExporterUtil.writeData(table, fileType, response.getOutputStream(), charset, separator);
        context.getResponseComplete();
    }

    public static void facesDownloadFileTxt(FileTxt file, String filename, String charset, boolean zipFile) throws IOException {
        if (zipFile) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            facesDownloadZip(baos, filename);
            return;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        configureContentAndHeaders(filename, TXT, response);
        ExporterUtil.writeFileTxtToOutput(file, charset, response.getOutputStream());
        context.getResponseComplete();
    }

    public static void facesDownloadZip(ByteArrayOutputStream baos, String filename) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        facesDownloadZip(baos, filename, response);
        context.getResponseComplete();
    }

    private static void facesDownloadZip(ByteArrayOutputStream baos, String filename, HttpServletResponse response) throws IOException {
        String zipFilename = FileType.ZIP.toFilenameWithExtension(filename);
        configureContentAndHeaders(zipFilename, FileType.ZIP, response);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            zipOutputStream.putNextEntry(new ZipEntry(filename));
            zipOutputStream.write(baos.toByteArray());
            zipOutputStream.closeEntry();
        }
    }

    public static void facesDownloadXls(String name, Table t) throws IOException {
        ExportServletUtil.facesDownloadResponse(t, name + ".xls", FileType.XLS, false);
    }

    public static void facesDownloadXlsx(String name, Table t) throws IOException {
        ExportServletUtil.facesDownloadResponse(t, name + ".xlsx", FileType.XLSX, false);
    }

    public static void facesDownloadCsv(String name, Table t, String chartsetName) throws IOException {
        String filename = FileType.CSV.toFilenameWithExtensionAndLocalTimeNow(name, DATE_TIME_FILE_NAME);
        ExportServletUtil.facesDownloadResponse(t, filename, FileType.CSV, chartsetName, false);
    }

    public static void facesDownloadTxt(String name, Table t, String chartsetName) throws IOException {
        String filename = TXT.toFilenameWithExtensionAndLocalTimeNow(name, DATE_TIME_FILE_NAME);
        ExportServletUtil.facesDownloadResponse(t, filename, TXT, chartsetName, false);
    }

    public static void facesDownloadCsvZip(String name, Table t, String charsetName) throws IOException {
        String filename = FileType.CSV.toFilenameWithExtensionAndLocalTimeNow(name, DATE_TIME_FILE_NAME);
        ExportServletUtil.facesDownloadResponse(t, filename, FileType.CSV, charsetName, true);
    }

}
