/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse;

import static br.com.tecsinapse.exporter.type.FileType.*;

import java.io.File;

import br.com.tecsinapse.exporter.type.FileType;

public enum ResourceFiles {
    MOCK_PLANILHA_CSV("/files/mock/planilha.csv", CSV),
    MOCK_PLANILHA_XLS("/files/mock/planilha.xls", XLS),
    MOCK_PLANILHA_XLSX("/files/mock/planilha.xlsx", XLSX),
    MOCK_PLANILHA_COM_PRIMEIRA_ABA_INVISIVEL_XLS("/files/mock/planilha-com-primeira-aba-invisivel.xls", XLS),
    MOCK_PLANILHA_COM_PRIMEIRA_ABA_INVISIVEL_XLSX("/files/mock/planilha-com-primeira-aba-invisivel.xlsx", XLSX),
    EXCEL_XLS("/files/excel.xls", XLS),
    EXCEL_XLSX("/files/excel.xlsx", XLSX),
    EXCEL_DATES_XLSX("/files/excel-dates.xlsx", XLSX),
    EXCEL_NUMERIC_XLSX("/files/excel-numeric.xlsx", XLSX),
    EXCEL_WITH_EMTPY_LINES_XLS("/files/excel-with-empty-lines.xls", XLS),
    EXCEL_WITH_EMPTY_LINES_XLSX("/files/excel-with-empty-lines.xlsx", XLSX),
    FIXED_LENGTH_FILE_TXT("/files/fixed-length-file.txt", TXT),
    FIXED_LENGTH_FILE_WITH_ERROR_TXT("/files/fixed-length-file-with-error.txt", TXT),
    FIXED_LENGTH_FILE_WITH_HEADER_DAT("/files/fixed-length-file-with-header.dat", TXT),
    FIXED_LENGTH_FIEL_WITH_HEADER_TXT("/files/fixed-length-file-with-header.txt", TXT),
    FIXED_LENGTH_FILE_WITH_LONG_HEADER_TXT("/files/fixed-length-file-with-long-header.txt", TXT),
    FIXED_LENGTH_FILE_WITH_LONG_HEADER_AND_EOF_TXT("/files/fixed-length-file-with-long-header-and-eof.txt", TXT),
    FIXED_LENGTH_FILE_WITH_SPACE_LINE_AND_LONG_HEADER_AND_EOF_TXT("/files/fixed-length-file-with-space-line-and-long-header-and-eof.txt", TXT),
    TESTE_SEM_RAZAO_XLSX("/files/teste_sem_razao.xlsx", XLSX);

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
}
