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

import br.com.tecsinapse.exporter.type.FileType;

/**
 * Does some thing in old style. It will be removed in version 1.6.1.
 *
 * @deprecated use {@link br.com.tecsinapse.exporter.type.FileType}
 */
@Deprecated
public enum ExcelType {

    XLS("dd/MM/yyyy", "dd/MM/yyyy HH:mm:ss", FileType.XLS),
    XLSX("MM/dd/yyyy", "MM/dd/yyyy HH:mm:ss", FileType.XLSX),
    XLSM("MM/dd/yyyy", "MM/dd/yyyy HH:mm:ss", FileType.XLSM);

    private final String defaultDatePattern;
    private final String defaultDateTimePattern;
    private final FileType fileType;

    ExcelType(String defaultDatePattern, String defaultDateTimePattern, FileType fileType) {
        this.defaultDatePattern = defaultDatePattern;
        this.defaultDateTimePattern = defaultDateTimePattern;
        this.fileType = fileType;
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

    public FileType getFileType() {
        return fileType;
    }
}
