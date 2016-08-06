/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.type;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.ResourceFiles;

public class FileTypeTest {

    @DataProvider(name = "fileTypeDs")
    private Object[][] fileTypeDs() {
        return new Object[][] {
                {"XLS", ResourceFiles.MOCK_PLANILHA_XLS, FileType.XLS, HSSFWorkbook.class},
                {"XLSX", ResourceFiles.MOCK_PLANILHA_XLSX, FileType.XLSX, XSSFWorkbook.class},
                {"XLSM", ResourceFiles.EXCEL_XLSM, FileType.XLSM, XSSFWorkbook.class},
                {"CSV", ResourceFiles.MOCK_PLANILHA_CSV, FileType.CSV, null},
                {"TXT", ResourceFiles.FIXED_LENGTH_FILE_TXT, FileType.TXT, null},
                {"TXT", ResourceFiles.FIXED_LENGTH_FILE_WITH_HEADER_DAT, FileType.TXT, null}
        };
    }

    @Test(dataProvider = "fileTypeDs")
    public void getFileTypeTest(String fromString, ResourceFiles resourceFile, FileType expected, Class<Workbook> wkExpected) throws Exception {
        FileType actualFromString = FileType.valueOf(fromString);
        Assert.assertEquals(actualFromString, expected, String.format("%s - %s", actualFromString.getDescription(), actualFromString.getDescription()));

        FileType actualFromFileName = FileType.getFileType(resourceFile.getFileName());
        Assert.assertEquals(actualFromFileName, expected, String.format("%s - %s", actualFromFileName.getDescription(), actualFromFileName.getDescription()));

        Workbook wk = actualFromFileName.buildWorkbook(new FileInputStream(resourceFile.getFile()));
        if (wkExpected == null) {
            Assert.assertNull(wk, resourceFile.getFileName());
        } else {
            Assert.assertTrue(wkExpected.isInstance(wk), resourceFile.getFileName());
        }
    }

}