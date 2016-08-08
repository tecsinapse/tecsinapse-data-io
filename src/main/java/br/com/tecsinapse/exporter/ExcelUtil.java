/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter;

import static br.com.tecsinapse.exporter.type.SeparatorType.SEMICOLON;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import br.com.tecsinapse.exporter.servlet.ExportServletUtil;
import br.com.tecsinapse.exporter.util.ExporterUtil;
import br.com.tecsinapse.exporter.util.SpreadsheetUtil;

/**
 * This class is moved. It will be removed in version 2.0.0
 *
 * @deprecated use {@link SpreadsheetUtil}
 */
@Deprecated
public class ExcelUtil {

    private ExcelUtil() {

    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil}
     */
    @Deprecated
    public static void exportXls(String name, Table t) throws IOException {
        ExportServletUtil.facesDownloadXls(name, t);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil}
     */
    @Deprecated
    public static void exportXlsx(String name, Table t) throws IOException {
        ExportServletUtil.facesDownloadXlsx(name, t);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil}
     */
    @Deprecated
    public static void exportSXlsx(String name, Table t) throws IOException {
        ExportServletUtil.facesDownloadXlsx(name, t);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil}
     */
    @Deprecated
    public static void exportCsv(String name, Table t, String chartsetName) throws IOException {
        ExportServletUtil.facesDownloadCsv(name, t, chartsetName);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil}
     */
    @Deprecated
    public static void exportTxt(String name, Table t, String chartsetName) throws IOException {
        ExportServletUtil.facesDownloadTxt(name, t, chartsetName);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil}
     */
    @Deprecated
    public static void exportCsvZip(String name, Table t, String charsetName) throws IOException {
        ExportServletUtil.facesDownloadCsvZip(name, t, charsetName);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     */
    @Deprecated
    public static void exportCsv(Table t, String chartsetName, OutputStream out) throws IOException {
        ExporterUtil.writeCsvToOutput(t, chartsetName, out);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     */
    @Deprecated
    public static File getCsvFile(Table t, String fileName, String charsetName) throws IOException {
        return ExporterUtil.getCsvFile(t, fileName, charsetName, SEMICOLON.getSeparator());
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     */
    @Deprecated
    public static File getCsvFile(Table t, File f, String charsetName) throws IOException {
        return ExporterUtil.getCsvFile(t, f, charsetName, SEMICOLON.getSeparator());
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     */
    @Deprecated
    public static File getSvFile(Table t, String file, String charsetName, char separator) throws IOException {
        return ExporterUtil.getCsvFile(t, file, charsetName, separator);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     */
    @Deprecated
    public static File getXlsFile(Table t, String file) throws IOException {
        return ExporterUtil.getXlsFile(t, file);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     */
    @Deprecated
    public static File getXlsxFile(Table t, String file) throws IOException {
        return ExporterUtil.getXlsxFile(t, file);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.SpreadsheetUtil}
     */
    @Deprecated
    public static int getColumnIndexByColumnName(String columnName) {
        return SpreadsheetUtil.getColumnIndexByColumnName(columnName);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.SpreadsheetUtil}
     */
    @Deprecated
    public static String getColumnNameByColumnIndex(int columnIndex) {
        return SpreadsheetUtil.getColumnNameByColumnIndex(columnIndex);
    }
}
