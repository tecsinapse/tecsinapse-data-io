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
     * This method is moved. It will be removed. Use dependency "tecsinapse-exporter-jsf"
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil.facesDownloadXls(name, t)}
     * @throws IOException IOException IOException
     */
    @Deprecated
    public static void exportXls() throws IOException {
    }

    /**
     * This method is moved. It will be removed. Use dependency "tecsinapse-exporter-jsf"
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil.facesDownloadXlsx(name, t)}
     * @throws IOException IOException IOException
     */
    @Deprecated
    public static void exportXlsx() throws IOException {
        ;
    }

    /**
     * This method is moved. It will be removed. Use dependency "tecsinapse-exporter-jsf"
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil.facesDownloadXlsx(name, t)}
     * @throws IOException IOException IOException
     */
    @Deprecated
    public static void exportSXlsx() throws IOException {
        ;
    }

    /**
     * This method is moved. It will be removed. Use dependency "tecsinapse-exporter-jsf"
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil.facesDownloadCsv(name, t, charsetName)}
     * @throws IOException IOException IOException
     */
    @Deprecated
    public static void exportCsv() throws IOException {

    }

    /**
     * This method is moved. It will be removed. Use dependency "tecsinapse-exporter-jsf"
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil.facesDownloadTxt(name, t, charsetName)}
     * @throws IOException IOException IOException
     */
    @Deprecated
    public static void exportTxt() throws IOException {

    }

    /**
     * This method is moved. It will be removed. Use dependency "tecsinapse-exporter-jsf"
     *
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil.facesDownloadCsvZip(name, t, charsetName)}
     * @throws IOException IOException IOException
     */
    @Deprecated
    public static void exportCsvZip() throws IOException {

    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param t table
     * @param charsetName charset
     * @param out out
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     * @throws IOException IOException IOException
     */
    @Deprecated
    public static void exportCsv(Table t, String charsetName, OutputStream out) throws IOException {
        ExporterUtil.writeCsvToOutput(t, charsetName, out);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param t table
     * @param fileName fileName
     * @param charsetName charset
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     * @return File
     * @throws IOException IOException
     */
    @Deprecated
    public static File getCsvFile(Table t, String fileName, String charsetName) throws IOException {
        return ExporterUtil.getCsvFile(t, fileName, charsetName, SEMICOLON.getSeparator());
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param t table
     * @param f file
     * @param charsetName charset
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     * @return File
     * @throws IOException IOException
     */
    @Deprecated
    public static File getCsvFile(Table t, File f, String charsetName) throws IOException {
        return ExporterUtil.getCsvFile(t, f, charsetName, SEMICOLON.getSeparator());
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param t table
     * @param file file
     * @param charsetName charset
     * @param separator
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     * @return File
     * @throws IOException IOException
     */
    @Deprecated
    public static File getSvFile(Table t, String file, String charsetName, char separator) throws IOException {
        return ExporterUtil.getCsvFile(t, file, charsetName, separator);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param t table
     * @param file file
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     * @return File
     * @throws IOException IOException
     */
    @Deprecated
    public static File getXlsFile(Table t, String file) throws IOException {
        return ExporterUtil.getXlsFile(t, file);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param t table
     * @param file file
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.ExporterUtil}
     * @return File
     * @throws IOException IOException
     */
    @Deprecated
    public static File getXlsxFile(Table t, String file) throws IOException {
        return ExporterUtil.getXlsxFile(t, file);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param columnName columnName
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.SpreadsheetUtil}
     * @return int
     */
    @Deprecated
    public static int getColumnIndexByColumnName(String columnName) {
        return SpreadsheetUtil.getColumnIndexByColumnName(columnName);
    }

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param columnIndex columnIndex
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.util.SpreadsheetUtil}
     * @return String
     */
    @Deprecated
    public static String getColumnNameByColumnIndex(int columnIndex) {
        return SpreadsheetUtil.getColumnNameByColumnIndex(columnIndex);
    }
}
