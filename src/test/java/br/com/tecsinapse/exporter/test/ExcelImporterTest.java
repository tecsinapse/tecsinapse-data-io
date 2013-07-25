package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.importer.ExcelImporter;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class ExcelImporterTest {
    @DataProvider(name = "excel")
    public Object[][] getExcel() throws URISyntaxException {

        return new Object[][]{
                {getFile("teste_sem_razao.xlsx"), "FakePojo{one='01582044000725', two='', three='02/07/2012'}FakePojo{one='71444475001600', two='AUTOMEC COMERCIAL DE VEICULOS LTDA', three='02/07/2012'}"},
                {getFile("excel.xls"), "FakePojo{one='ABC', two='DEF', three='GHI'}FakePojo{one='XXX', two='', three='XXX'}"},
                {getFile("excel.xlsx"), "FakePojo{one='ABC', two='DEF', three='GHI'}FakePojo{one='XXX', two='', three='XXX'}"}
        };
    }

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/" + name).toURI());
    }

    @Test(dataProvider = "excel")
    public void test(File file, String result) throws Exception {
        List<FakePojo> pojos = new ExcelImporter<FakePojo>(FakePojo.class, file).parse();
        StringBuilder sb = new StringBuilder();
        for (FakePojo fp : pojos) {
            sb.append(fp);
        }
        Assert.assertEquals(sb.toString(), result);
    }

}
