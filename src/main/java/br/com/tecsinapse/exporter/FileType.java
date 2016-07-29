/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

/**
 * Does some thing in old style. It will be removed in version 1.6.1.
 *
 * @deprecated use {@link br.com.tecsinapse.exporter.type.FileType}
 */
@Deprecated
public enum FileType {

    XLS(br.com.tecsinapse.exporter.type.FileType.XLS, ExcelType.XLS),
    XLSX(br.com.tecsinapse.exporter.type.FileType.XLSX, ExcelType.XLSX),
    XLSM(br.com.tecsinapse.exporter.type.FileType.XLSM, ExcelType.XLSM),
    CSV(br.com.tecsinapse.exporter.type.FileType.CSV, null);

    private final br.com.tecsinapse.exporter.type.FileType fileType;
    private final ExcelType excelType;

    FileType(br.com.tecsinapse.exporter.type.FileType fileType, ExcelType excelType) {
        this.fileType = fileType;
        this.excelType = excelType;
    }

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

    public br.com.tecsinapse.exporter.type.FileType toNewFileType() {
        return this.fileType;
    }

    public ExcelType getExcelType() {
        return excelType;
    }
}