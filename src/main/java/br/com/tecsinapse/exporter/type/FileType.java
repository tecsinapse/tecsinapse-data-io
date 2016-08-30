/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.type;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.tecsinapse.exporter.util.ExporterDateUtils;

public enum FileType {

    XLS("MS Excel file (.xls)", "application/vnd.ms-excel", ".xls") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return new HSSFWorkbook(inputStream);
        }
    },
    XLSX("MS Excel file (.xlsx)", "application/vnd.ms-excel", ".xlsx") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return new XSSFWorkbook(inputStream);
        }
    },
    XLSM("MS Excel file (.xlsm)", "application/vnd.ms-excel", ".xlsm") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return XLSX.buildWorkbook(inputStream);
        }
    },
    CSV("CSV file", "text/plain", ".csv"),
    TXT("Text file", "text/plain", ".txt"),
    ZIP("Zip file", "application/zip", ".zip");

    private final String description;
    private final String mimeType;
    private final String extension;

    FileType(String description, String mimeType, String extension) {
        this.description = description;
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public static FileType getFileType(String filename) {
        for (FileType fileType : values()) {
            if (filename.toLowerCase().endsWith(fileType.getExtension().toLowerCase())) {
                return fileType;
            }
        }
        return TXT;
    }

    public String getDescription() {
        return description;
    }

    public Workbook buildWorkbook(InputStream inputStream) throws IOException {
        return null;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String toFilenameWithExtension(String filename) {
        if (filename == null || filename.toLowerCase().endsWith(extension.toLowerCase())) {
            return filename;
        }
        return filename + extension;
    }

    public String toFilenameWithExtensionAndLocalTimeNow(String filename) {
        String filenameWithExtension = toFilenameWithExtension(filename);
        if (filenameWithExtension == null) {
            return filenameWithExtension;
        }
        String now = ExporterDateUtils.formatAsFileDateTime(new Date());
        String filenameOnly = filenameWithExtension.length() >= extension.length() ? filenameWithExtension.substring(0, filenameWithExtension.length() - extension.length()) : filenameWithExtension;
        return  filenameOnly + "_" +  now  + extension;
    }
}
