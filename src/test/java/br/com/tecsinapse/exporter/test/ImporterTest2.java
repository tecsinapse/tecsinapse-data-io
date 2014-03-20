package br.com.tecsinapse.exporter.test;

import static br.com.tecsinapse.exporter.importer.ImporterXLSXType.UNIQUE_DATA_VALUE;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.importer.Importer;

public class ImporterTest2 {
	
    @DataProvider(name = "arquivos")
    public Object[][] getExcel() throws URISyntaxException {

        return new Object[][]{
                {getFile("excel-dates.xlsx"), "FakePojo2{data1='2014-02-15', data2='2013-05-05', data3='2015-07-04'}FakePojo2{data1='2016-06-16', data2='1996-10-23', data3='null'}"},
        };
    }

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/" + name).toURI());
    }

    @Test(dataProvider = "arquivos")
    public void test(File file, String result) throws Exception {
        List<FakePojo2> pojos = new Importer<FakePojo2>(FakePojo2.class, file, UNIQUE_DATA_VALUE).parse();
        StringBuilder sb = new StringBuilder();
        for (FakePojo2 fp : pojos) {
            sb.append(fp);
        }
        Assert.assertEquals(sb.toString(), result);
    }

}
