package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.importer.CsvImporter;
import com.google.common.collect.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: filipe
 * Date: 7/23/13
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class CsvImporterTest {
    @DataProvider(name = "csvs")
    public Object[][] getCsvs() {
        return new Object[][] {
                {Lists.<String>newArrayList("ABC;DEF;GHI", "XXX;YYY;ZZZ")}
        };
    }

    @Test(dataProvider = "csvs")
    public void test(List<String> lines) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<FakePojo> pojos = new CsvImporter<FakePojo >(FakePojo.class, lines).parse();
        System.out.println(pojos);
    }
}
