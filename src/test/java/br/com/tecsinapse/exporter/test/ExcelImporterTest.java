package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.importer.ExcelImporter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: filipe
 * Date: 7/23/13
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelImporterTest {
    @DataProvider(name = "excel")
    public Object[][] getExcel() throws URISyntaxException {
        File file = new File(getClass().getResource("/files/excel.xls").toURI());

        return new Object[][] {
                {
                        file
                }
        };
    }

    @Test(dataProvider = "excel")
    public void test(File file) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException, InvalidFormatException {
        List<FakePojo> pojos = new ExcelImporter<FakePojo>(FakePojo.class, file).parse();
        System.out.println(pojos);
    }
}
