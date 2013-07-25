package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

public class ExcelUtilTest {

    @DataProvider(name = "columnNames")
    public Object[][] columnNames() throws URISyntaxException {

        return new Object[][]{
                {"A1", 0},
                {"B", 1},
        };
    }

    @Test(dataProvider = "columnNames")
    public void getColumnIndex(String columnName, int result) {
        Assert.assertEquals(ExcelUtil.getColumnIndexByColumnName(columnName), result);
    }

    @DataProvider(name = "columnIndexes")
    public Object[][] columnIndexes() throws URISyntaxException {

        return new Object[][]{
                {0, "A"},
                {1, "B"},
        };
    }

    @Test(dataProvider = "columnIndexes")
    public void getColumnName(int columnIndex, String result) {
        Assert.assertEquals(ExcelUtil.getColumnNameByColumnIndex(columnIndex), result);
    }
}

