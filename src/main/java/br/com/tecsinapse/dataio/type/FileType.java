/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.type;

import static br.com.tecsinapse.dataio.util.Constants.MIME_XLSX_XLS;
import static br.com.tecsinapse.dataio.util.Constants.MSG_IGNORED;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.AllArgsConstructor;
import lombok.Getter;

import br.com.tecsinapse.dataio.util.ExporterDateUtils;

@Getter
@AllArgsConstructor
public enum FileType {

    XLS("MS Excel file (.xls)", MIME_XLSX_XLS, ".xls") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return new HSSFWorkbook(inputStream);
        }
    },
    XLSX("MS Excel file (.xlsx)", MIME_XLSX_XLS, ".xlsx") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return new XSSFWorkbook(inputStream);
        }
    },
    SXLSX("MS Excel file (.xlsx)", MIME_XLSX_XLS, ".xlsx") {
        @Override
        public Workbook buildWorkbook(InputStream inputStream) throws IOException {
            return new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        }
    },
    XLSM("MS Excel file (.xlsm)", MIME_XLSX_XLS, ".xlsm") {
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

    public static FileType getFileType(String filename) {
        for (FileType fileType : values()) {
            if (filename.toLowerCase().endsWith(fileType.getExtension().toLowerCase())) {
                return fileType;
            }
        }
        return TXT;
    }

    public Workbook buildWorkbook(InputStream inputStream) throws IOException {
        throw new UnsupportedOperationException(MSG_IGNORED);
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

    /**
     * For compatibility
     *
     * @deprecated
     */
    @Deprecated
    public FileType getExcelType() {
        return this;
    }

    /**
     * For compatibility
     *
     * @deprecated
     */
    @Deprecated
    public static FileType getExcelType(String filename) {
        return getFileType(filename);
    }
}
