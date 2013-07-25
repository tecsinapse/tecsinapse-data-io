package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.importer.CsvImporter;
import com.google.common.collect.Lists;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CsvImporterTest {
    @DataProvider(name = "csvs")
    public Object[][] getCsvs() {
        return new Object[][] {
                {Lists.<String>newArrayList("ABC;DEF;GHI", "XXX;YYY;ZZZ"), "FakePojo{one='ABC', two='DEF', three='GHI'}FakePojo{one='XXX', two='YYY', three='ZZZ'}"}
        };
    }

    @Test(dataProvider = "csvs")
    public void test(List<String> lines, String result) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<FakePojo> pojos = new CsvImporter<FakePojo >(FakePojo.class, lines).parse();
        StringBuilder sb = new StringBuilder();
        for (FakePojo fp : pojos) {
            sb.append(fp);
        }
        Assert.assertEquals(sb.toString(), result);
    }
}
