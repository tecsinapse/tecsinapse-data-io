/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

public enum FileType {

    XLS,
    XLSX,
    XLSM,
    CSV;

    public static FileType getFileType(String filename) {
        if (filename.toLowerCase().endsWith("xlsx")) {
            return FileType.XLSX;
        } else if (filename.toLowerCase().endsWith("xls")) {
            return FileType.XLS;
        } else if (filename.toLowerCase().endsWith("xlsm")) {
            return FileType.XLSM;
        }
        return CSV;
    }

    public ExcelType getExcelType() {
        if (this == XLS) {
            return ExcelType.XLS;
        }
        if (this == XLSX) {
            return ExcelType.XLSX;
        }
        if (this == XLSM) {
            return ExcelType.XLSM;
        }
        return null;
    }

}
