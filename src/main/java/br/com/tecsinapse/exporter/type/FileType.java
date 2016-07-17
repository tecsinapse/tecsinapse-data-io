/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.type;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public enum FileType {

    XLS("MS Excel file (.xls)") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return new HSSFWorkbook(inputStream);
        }
    },
    XLSX("MS Excel file (.xlsx)") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return new XSSFWorkbook(inputStream);
        }
    },
    CSV("CSV file"),
    TXT("Text file");

    private final String description;

    FileType(String description) {
        this.description = description;
    }

    public static FileType getFileType(String filename) {
        if (filename.toLowerCase().endsWith(".xls")) {
            return XLS;
        }
        if (filename.toLowerCase().endsWith(".xlsx")) {
            return XLSX;
        }
        if (filename.toLowerCase().endsWith(".csv")) {
            return CSV;
        }
        return TXT;
    }

    public String getDescription() {
        return description;
    }

    public Workbook buildWorkbook(InputStream inputStream) throws IOException {
        return null;
    }

}
