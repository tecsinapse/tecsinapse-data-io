package br.com.tecsinapse.exporter.test;

import static br.com.tecsinapse.exporter.importer.ImporterXLSXType.UNIQUE_DATA_VALUE;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.importer.Importer;

public class ImporterTestNumeric {
	
    @DataProvider(name = "arquivos")
    public Object[][] getExcel() throws URISyntaxException {

        return new Object[][]{
                {getFile("excel-numeric.xlsx"), "FakePojoNumeric{value1='1323.45', value2='2433.56', value3='2014.0', value4='665.0', value5='4345.0'}FakePojoNumeric{value1='0.5', value2='4354.0', value3='null', value4='34.56', value5='454.0'}"},
        };
    }

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/" + name).toURI());
    }

    @Test(dataProvider = "arquivos")
    public void test(File file, String result) throws Exception {
        List<FakePojoNumeric> pojos = new Importer<FakePojoNumeric>(FakePojoNumeric.class, file, UNIQUE_DATA_VALUE).parse();
        StringBuilder sb = new StringBuilder();
        for (FakePojoNumeric fp : pojos) {
            sb.append(fp);
        }
        Assert.assertEquals(sb.toString(), result);
    }

}
