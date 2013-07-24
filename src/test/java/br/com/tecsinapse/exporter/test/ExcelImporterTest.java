package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.importer.ExcelImporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class ExcelImporterTest {
    @DataProvider(name = "excel")
    public Object[][] getExcel() throws URISyntaxException {
        File xls = new File(getClass().getResource("/files/excel.xls").toURI());
        File xlsx = new File(getClass().getResource("/files/excel.xlsx").toURI());

        return new Object[][] {
                {xls}, {xlsx}
        };
    }

    @Test(dataProvider = "excel")
    public void test(File file) throws Exception {
        List<FakePojo> pojos = new ExcelImporter<FakePojo>(FakePojo.class, file).parse();
        System.out.println(pojos);
    }
}
