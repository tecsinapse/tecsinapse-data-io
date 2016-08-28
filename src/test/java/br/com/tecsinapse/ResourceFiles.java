/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse;

import static br.com.tecsinapse.exporter.type.FileType.CSV;
import static br.com.tecsinapse.exporter.type.FileType.TXT;
import static br.com.tecsinapse.exporter.type.FileType.XLS;
import static br.com.tecsinapse.exporter.type.FileType.XLSM;
import static br.com.tecsinapse.exporter.type.FileType.XLSX;
import static br.com.tecsinapse.exporter.type.FileType.ZIP;

import java.io.File;

import br.com.tecsinapse.datasources.DataParser;
import br.com.tecsinapse.datasources.FileDataParser;
import br.com.tecsinapse.exporter.type.FileType;
@SuppressWarnings("unchecked")
public enum ResourceFiles {
    MOCK_PLANILHA_CSV("/files/mock/planilha.csv", CSV),
    MOCK_PLANILHA_XLS("/files/mock/planilha.xls", XLS),
    MOCK_PLANILHA_XLSX("/files/mock/planilha.xlsx", XLSX),
    MOCK_PLANILHA_COM_PRIMEIRA_ABA_INVISIVEL_XLS("/files/mock/planilha-com-primeira-aba-invisivel.xls", XLS),
    MOCK_PLANILHA_COM_PRIMEIRA_ABA_INVISIVEL_XLSX("/files/mock/planilha-com-primeira-aba-invisivel.xlsx", XLSX),
    EXCEL_XLS("/files/excel.xls", XLS),
    EXCEL_XLSX("/files/excel.xlsx", XLSX),
    EXCEL_XLSM("/files/excel.xlsm", XLSM),
    EXCEL_DATES_XLSX("/files/excel-dates.xlsx", XLSX),
    EXCEL_NUMERIC_XLSX("/files/excel-numeric.xlsx", XLSX),
    EXCEL_WITH_EMPTY_LINES_XLS("/files/excel-with-empty-lines.xls", XLS) {
        @Override
        public FileDataParser getFileDataParser() {
            FileDataParser fileDataParser = new FileDataParser(this.getFile(), getFileType());
            fileDataParser.newSheet()
                    .addRow(new DataParser("2016-01-01", "2016-01-01T00:13:15", "0.5", 127, "Line 1", "", "0:47"))
                    .addRow(new DataParser("2016-02-01", "2016-02-01T01:13:15", "0.568", 135, "Line 2", "", "1:35"))
                    .addRow(new DataParser("2016-03-01", "2016-03-01T02:13:15", "0.478", 143, "Line 3", "", "2:23"))
                    .addRow(new DataParser("2016-04-01", "2016-04-01T03:13:15", "0.165", 151, "Line 4", "", "3:11"))
                    .addRow(new DataParser("2016-05-01", "2016-05-01T04:13:15", "156.23", 159, "Line 5", "", "3:59"))
                    .addRow(new DataParser("2016-06-01", "2016-06-01T05:13:15", "23", 167, "Line 6", "", "4:47"))
                    .addRow(new DataParser("2016-07-01", "2016-07-01T06:13:15", "25.68", 175, "Line 7", "", "5:35"))
                    .addRow(new DataParser("2016-08-01", "2016-08-01T07:13:15", "165.8", 183, "Line 8", "", "6:23"))
                    .addRow(new DataParser("2016-09-01", "2016-09-01T08:13:15", "145.23", 191, "Line 9", "", "7:11"))
                    .addRow(new DataParser("2016-10-01", "2016-10-01T09:13:15", "56.36", 199, "Line 10", "", "7:59"))
                    .addRow(new DataParser("2016-11-01", "2016-11-01T10:13:15", "1542.2", 207, "Line 11", "", "8:47"))
                    .addRow(new DataParser("2016-12-01", "2016-12-01T11:13:15", "5682.8", 215, "Line 12", "", "9:35"))
                    .addRow(new DataParser("2016-01-15", "2017-01-15T12:13:15", "2356.32", 223, "Line 13", "", "10:23"))
                    .addRow(new DataParser("2016-02-15", "2017-02-15T13:13:15", "46854.26", 231, "Line 14", "", "11:11"))
                    .addRow(new DataParser("2016-03-15", "2017-03-15T14:13:15", "987.35", 239, "Line 15", "", "11:59"))
                    .addRow(new DataParser("2016-04-15", "2017-04-15T15:13:15", "0.23", 247, "Line 16", "", "12:47"))
                    .addRow(new DataParser("2016-05-15", "2017-05-15T16:13:15", "0.478", 255, "Line 17", "", "13:35"))
                    .addRow(new DataParser("2016-06-15", "2017-06-15T17:13:15", "0.698", 263, "Line 18", "", "14:23"))
                    .addRow(new DataParser("2016-07-15", "2017-07-15T18:13:15", "325.6", 271, "Line 19", "", "15:11"))
                    .addRow(new DataParser("2016-08-15", "2017-08-15T19:13:15", "0.1346", 279, "Line 20", "", "15:59"))
                    .addRow(new DataParser("2016-09-15", "2017-09-15T20:13:15", "0.2653", 287, "Line 21", "", "16:47"))
                    .addRow(new DataParser("2016-10-15", "2017-10-15T21:13:15", "356", 295, "Line 22", "", "17:35"))
                    .addRow(new DataParser("2016-11-15", "2017-11-15T22:13:15", "0.3256", 303, "Line 23", "", "18:23"))
                    .addRow(new DataParser("2016-12-15", "2017-12-15T23:13:15", "0.2356", 311, "Line 24", "", "19:11"));
            fileDataParser.newSheet()
                    .addRow(new DataParser("2016-03-01", "2016-03-01T02:13:15", "0.478", 143, "Line 1", "", "2:23"))
                    .addRow(new DataParser("2016-06-01", "2016-06-01T05:13:15", "23", 167, "Line 2", "", "4:47"))
                    .addRow(new DataParser("2016-09-01", "2016-09-01T08:13:15", "145.23", 191, "Line 3", "", "7:11"))
                    .addRow(new DataParser("2016-12-01", "2016-12-01T11:13:15", "5682.8", 215, "Line 4", "", "9:35"))
                    .addRow(new DataParser("2016-01-15", "2017-01-15T12:13:15", "2356.32", 223, "Line 5", "", "10:23"))
                    .addRow(new DataParser("2016-04-15", "2017-04-15T15:13:15", "0.23", 247, "Line 6", "", "12:47"))
                    .addRow(new DataParser("2016-07-15", "2017-07-15T18:13:15", "325.6", 271, "Line 7", "", "15:11"))
                    .addRow(new DataParser("2016-10-15", "2017-10-15T21:13:15", "356", 295, "Line 8", "", "17:35"));
            return fileDataParser;
        }
    },
    EXCEL_WITH_EMPTY_LINES_XLSX("/files/excel-with-empty-lines.xlsx", XLSX) {
        @Override
        public FileDataParser getFileDataParser() {
            FileDataParser fileDataParser = new FileDataParser(this.getFile(), getFileType());
            fileDataParser.newSheet()
                    .addRow(new DataParser("2016-01-01", "2016-01-01T00:13:15", "0.5", 127, "Line 1", "", "0:47"))
                    .addRow(new DataParser("2016-02-01", "2016-02-01T01:13:15", "0.568", 135, "Line 2", "", "1:35"))
                    .addRow(new DataParser("2016-03-01", "2016-03-01T02:13:15", "0.478", 143, "Line 3", "", "2:23"))
                    .addRow(new DataParser("2016-04-01", "2016-04-01T03:13:15", "0.165", 151, "Line 4", "", "3:11"))
                    .addRow(new DataParser("2016-05-01", "2016-05-01T04:13:15", "156.23", 159, "Line 5", "", "3:59"))
                    .addRow(new DataParser("2016-06-01", "2016-06-01T05:13:15", "23", 167, "Line 6", "", "4:47"))
                    .addRow(new DataParser("2016-07-01", "2016-07-01T06:13:15", "25.68", 175, "Line 7", "", "5:35"))
                    .addRow(new DataParser("2016-08-01", "2016-08-01T07:13:15", "165.8", 183, "Line 8", "", "6:23"))
                    .addRow(new DataParser("2016-09-01", "2016-09-01T08:13:15", "145.23", 191, "Line 9", "", "7:11"))
                    .addRow(new DataParser("2016-10-01", "2016-10-01T09:13:15", "56.36", 199, "Line 10", "", "7:59"))
                    .addRow(new DataParser("2016-11-01", "2016-11-01T10:13:15", "1542.2", 207, "Line 11", "", "8:47"))
                    .addRow(new DataParser("2016-12-01", "2016-12-01T11:13:15", "5682.8", 215, "Line 12", "", "9:35"))
                    .addRow(new DataParser("2016-01-15", "2017-01-15T12:13:15", "2356.32", 223, "Line 13", "", "10:23"))
                    .addRow(new DataParser("2016-02-15", "2017-02-15T13:13:15", "46854.26", 231, "Line 14", "", "11:11"))
                    .addRow(new DataParser("2016-03-15", "2017-03-15T14:13:15", "987.35", 239, "Line 15", "", "11:59"))
                    .addRow(new DataParser("2016-04-15", "2017-04-15T15:13:15", "0.23", 247, "Line 16", "", "12:47"))
                    .addRow(new DataParser("2016-05-15", "2017-05-15T16:13:15", "0.478", 255, "Line 17", "", "13:35"))
                    .addRow(new DataParser("2016-06-15", "2017-06-15T17:13:15", "0.698", 263, "Line 18", "", "14:23"))
                    .addRow(new DataParser("2016-07-15", "2017-07-15T18:13:15", "325.6", 271, "Line 19", "", "15:11"))
                    .addRow(new DataParser("2016-08-15", "2017-08-15T19:13:15", "0.1346", 279, "Line 20", "", "15:59"))
                    .addRow(new DataParser("2016-09-15", "2017-09-15T20:13:15", "0.2653", 287, "Line 21", "", "16:47"))
                    .addRow(new DataParser("2016-10-15", "2017-10-15T21:13:15", "356", 295, "Line 22", "", "17:35"))
                    .addRow(new DataParser("2016-11-15", "2017-11-15T22:13:15", "0.3256", 303, "Line 23", "", "18:23"))
                    .addRow(new DataParser("2016-12-15", "2017-12-15T23:13:15", "0.2356", 311, "Line 24", "", "19:11"));
            fileDataParser.newSheet()
                    .addRow(new DataParser("2016-03-01", "2016-03-01T02:13:15", "0.478", 143, "Line 1", "", "2:23"))
                    .addRow(new DataParser("2016-06-01", "2016-06-01T05:13:15", "23", 167, "Line 2", "", "4:47"))
                    .addRow(new DataParser("2016-09-01", "2016-09-01T08:13:15", "145.23", 191, "Line 3", "", "7:11"))
                    .addRow(new DataParser("2016-12-01", "2016-12-01T11:13:15", "5682.8", 215, "Line 4", "", "9:35"))
                    .addRow(new DataParser("2016-01-15", "2017-01-15T12:13:15", "2356.32", 223, "Line 5", "", "10:23"))
                    .addRow(new DataParser("2016-04-15", "2017-04-15T15:13:15", "0.23", 247, "Line 6", "", "12:47"))
                    .addRow(new DataParser("2016-07-15", "2017-07-15T18:13:15", "325.6", 271, "Line 7", "", "15:11"))
                    .addRow(new DataParser("2016-10-15", "2017-10-15T21:13:15", "356", 295, "Line 8", "", "17:35"));
            return fileDataParser;
        }
    },
    FIXED_LENGTH_FILE_TXT("/files/fixed-length-file.txt", TXT),
    FIXED_LENGTH_FILE_WITH_ERROR_TXT("/files/fixed-length-file-with-error.txt", TXT),
    FIXED_LENGTH_FILE_WITH_HEADER_DAT("/files/fixed-length-file-with-header.dat", TXT),
    FIXED_LENGTH_FIEL_WITH_HEADER_TXT("/files/fixed-length-file-with-header.txt", TXT),
    FIXED_LENGTH_FILE_WITH_LONG_HEADER_TXT("/files/fixed-length-file-with-long-header.txt", TXT),
    FIXED_LENGTH_FILE_WITH_LONG_HEADER_AND_EOF_TXT("/files/fixed-length-file-with-long-header-and-eof.txt", TXT),
    FIXED_LENGTH_FILE_WITH_SPACE_LINE_AND_LONG_HEADER_AND_EOF_TXT("/files/fixed-length-file-with-space-line-and-long-header-and-eof.txt", TXT),
    TESTE_SEM_RAZAO_XLSX("/files/teste_sem_razao.xlsx", XLSX),
    EXCEL_XLS_ZIP("/files/excel.xls.zip", ZIP),;

    private final String fileName;
    private final FileType fileType;

    ResourceFiles(String fileName, FileType fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return new File(ResourceFiles.class.getResource(fileName).getFile());
    }

    public FileType getFileType() {
        return fileType;
    }

    public FileDataParser getFileDataParser() {
        return null;
    }
}
