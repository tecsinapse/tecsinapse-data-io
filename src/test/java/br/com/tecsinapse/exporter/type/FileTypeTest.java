/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.type;

import java.io.FileInputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.ResourceFiles;
import br.com.tecsinapse.exporter.util.ExporterDateUtils;

public class FileTypeTest {

    @DataProvider(name = "fileTypeDs")
    private Object[][] fileTypeDs() {
        return new Object[][] {
                {"XLS", ResourceFiles.MOCK_PLANILHA_XLS, FileType.XLS, HSSFWorkbook.class, "application/vnd.ms-excel"},
                {"XLSX", ResourceFiles.MOCK_PLANILHA_XLSX, FileType.XLSX, XSSFWorkbook.class, "application/vnd.ms-excel"},
                {"XLSM", ResourceFiles.EXCEL_XLSM, FileType.XLSM, XSSFWorkbook.class, "application/vnd.ms-excel"},
                {"CSV", ResourceFiles.MOCK_PLANILHA_CSV, FileType.CSV, null, "text/plain"},
                {"TXT", ResourceFiles.FIXED_LENGTH_FILE_TXT, FileType.TXT, null, "text/plain"},
                {"ZIP", ResourceFiles.EXCEL_XLS_ZIP, FileType.ZIP, null, "application/zip"}
        };
    }

    @DataProvider(name = "fileTypeExtensionDs")
    private Object[][] fileTypeExtensionDs() {
        return new Object[][] {
                {"file1.zip", "file1%s.zip", FileType.ZIP},
                {"file1.csv", "file1%s.csv", FileType.CSV},
                {"file1.txt", "file1%s.txt", FileType.TXT},
                {"file1.xls", "file1%s.xls", FileType.XLS},
                {"file1.xlsm", "file1%s.xlsm", FileType.XLSM},
                {"file1.xlsx", "file1%s.xlsx", FileType.XLSX},
                {"file1", "file1%s.zip", FileType.ZIP},
                {"file1", "file1%s.csv", FileType.CSV},
                {"file1", "file1%s.txt", FileType.TXT},
                {"file1", "file1%s.xls", FileType.XLS},
                {"file1", "file1%s.xlsm", FileType.XLSM},
                {"file1", "file1%s.xlsx", FileType.XLSX},
                {null, null, FileType.XLSX}
        };
    }

    @Test(dataProvider = "fileTypeDs")
    public void getFileTypeTest(String fromString, ResourceFiles resourceFile, FileType expected, Class<Workbook> wkExpected, String mimeTypeExpected) throws Exception {
        FileType actualFromString = FileType.valueOf(fromString);
        Assert.assertEquals(actualFromString, expected, String.format("%s - %s", actualFromString.getDescription(), actualFromString.getDescription()));

        FileType actualFromFileName = FileType.getFileType(resourceFile.getFileName());
        Assert.assertEquals(actualFromFileName, expected, String.format("%s - %s", actualFromFileName.getDescription(), actualFromFileName.getDescription()));

        Assert.assertEquals(actualFromFileName.getMimeType(), mimeTypeExpected);
        Workbook wk = actualFromFileName.buildWorkbook(new FileInputStream(resourceFile.getFile()));
        if (wkExpected == null) {
            Assert.assertNull(wk, resourceFile.getFileName());
        } else {
            Assert.assertTrue(wkExpected.isInstance(wk), resourceFile.getFileName());
        }
    }

    @Test(dataProvider = "fileTypeExtensionDs")
    public void testFileTypeWithExtension(String filename, String filenameExpected, FileType fileType) {
        if (filenameExpected != null) {
            filenameExpected = String.format(filenameExpected, "");
        }
        Assert.assertEquals(fileType.toFilenameWithExtension(filename), filenameExpected);
    }

    @Test(dataProvider = "fileTypeExtensionDs")
    public void testFileTypeWithExtensionAndLocalTimeNow(String filename, String filenameExpected, FileType fileType) {
        if (filenameExpected != null) {
            filenameExpected = String.format(filenameExpected, "_" + ExporterDateUtils.formatAsFileDateTime(new Date()));
        }
        Assert.assertEquals(fileType.toFilenameWithExtensionAndLocalTimeNow(filename), filenameExpected);
    }

}