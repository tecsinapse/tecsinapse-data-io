/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.type;

@Deprecated
public enum ExcelType {

    XLS("dd/MM/yyyy", "dd/MM/yyyy HH:mm:ss"),
    XLSX("MM/dd/yyyy", "MM/dd/yyyy HH:mm:ss"),
    XLSM("MM/dd/yyyy", "MM/dd/yyyy HH:mm:ss");

    private final String defaultDatePattern;
    private final String defaultDateTimePattern;

    ExcelType(String defaultDatePattern, String defaultDateTimePattern) {
        this.defaultDatePattern = defaultDatePattern;
        this.defaultDateTimePattern = defaultDateTimePattern;
    }

    public static ExcelType getExcelType(String filename) {
        if (filename.toLowerCase().endsWith("xlsx")) {
            return ExcelType.XLSX;
        }
        if (filename.toLowerCase().endsWith("xlsm")) {
            return ExcelType.XLSM;
        }
        return ExcelType.XLS;
    }

    public String getDefaultDatePattern() {
        return defaultDatePattern;
    }

    public String getDefaultDateTimePattern() {
        return defaultDateTimePattern;
    }

}
