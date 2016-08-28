/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.converter.group.Default;
import br.com.tecsinapse.exporter.importer.Importer;

public class ImporterTest {

    @DataProvider(name = "arquivos")
    public Object[][] getExcel() throws URISyntaxException {
        return new Object[][]{
                {getFile("teste_sem_razao.xlsx"), getResultadoTesteSemRazaoXlsx(), Default.class},
                {getFile("excel.xls"), getResultadoExcel(), Default.class},
                {getFile("excel.xlsx"), getResultadoExcel(), Default.class},
        };
    }

    @DataProvider(name = "arquivos_default_gorup")
    public Object[][] getExcelDefaultGroup() throws URISyntaxException {
        return new Object[][]{
                {getFile("teste_sem_razao.xlsx"), getResultadoSemRazaoDefaultGroupXls(), Default.class},
                {getFile("excel.xls"), getResultadoDefaultGroupExcel(), Default.class},
                {getFile("excel.xlsx"), getResultadoDefaultGroupExcel(), Default.class},
        };
    }

    @DataProvider(name = "arquivos_extended_group")
    public Object[][] getExcelExtendedGroup() throws URISyntaxException {
        return new Object[][]{
                {getFile("teste_sem_razao.xlsx"), getResultadoSemRazaoExtendedGroupXls(), TestDefaultExtendedGroup.class},
                {getFile("excel.xls"), getResultadoExtendedGroupExcel(), TestDefaultExtendedGroup.class},
                {getFile("excel.xlsx"), getResultadoExtendedGroupExcel(), TestDefaultExtendedGroup.class}
        };
    }

    @DataProvider(name = "arquivos_custom_group")
    public Object[][] getExcelCustomGroup() throws URISyntaxException {
        return new Object[][]{
                {getFile("teste_sem_razao.xlsx"), getResultadoSemRazaoCustomGroupXls(), TestGroup.class},
                {getFile("excel.xls"), getResultadoCustomGroupExcel(), TestGroup.class},
                {getFile("excel.xlsx"), getResultadoCustomGroupExcel(), TestGroup.class},
        };
    }

    @Test(dataProvider = "arquivos")
    public void test(File file, String result, Class<?> group) throws Exception {
        StringBuilder sb = fileFakePojoGroupsToString(file, group, FakePojo.class);
        Assert.assertEquals(sb.toString(), result);
    }

    @Test(dataProvider = "arquivos_default_gorup")
    public void testDefaultGroups(File file, String result, Class<?> group) throws Exception {
        StringBuilder sb = fileFakePojoGroupsToString(file, group, FakePojoGroups.class);
        Assert.assertEquals(sb.toString(), result);
    }

    @Test(dataProvider = "arquivos_extended_group")
    public void testExtendedGroups(File file, String result, Class<?> group) throws Exception {
        StringBuilder sb = fileFakePojoGroupsToString(file, group, FakePojoGroups.class);
        Assert.assertEquals(sb.toString(), result);
    }

    @Test(dataProvider = "arquivos_custom_group")
    public void testCustomGroups(File file, String result, Class<?> group) throws Exception {
        StringBuilder sb = fileFakePojoGroupsToString(file, group, FakePojoGroups.class);
        Assert.assertEquals(sb.toString(), result);
    }

    private <T> StringBuilder fileFakePojoGroupsToString(File file, Class<?> group, Class<T> clazz) throws Exception {
        List<T> pojos = new Importer<>(clazz, file, group).parse();
        StringBuilder sb = new StringBuilder();
        for (T fp : pojos) {
            sb.append(fp);
        }
        return sb;
    }

    private String getResultadoExcel() {
        return "FakePojo{one='ABC', two='DEF', three='GHI'}FakePojo{one='XXX', two='', three='XXX'}";
    }

    private String getResultadoTesteSemRazaoXlsx() {
        return "FakePojo{one='01582044000725', two='', three='02/07/2012'}FakePojo{one='71444475001600', two='AUTOMEC COMERCIAL DE VEICULOS LTDA', three='02/07/2012'}";
    }

    private String getResultadoSemRazaoCustomGroupXls() {
        return "FakePojoGroups{one='', two='01582044000725', three='null'}FakePojoGroups{one='AUTOMEC COMERCIAL DE VEICULOS LTDA', two='71444475001600', three='null'}";
    }

    private String getResultadoCustomGroupExcel() {
        return "FakePojoGroups{one='DEF', two='ABC', three='null'}FakePojoGroups{one='', two='XXX', three='null'}";
    }

    private String getResultadoSemRazaoDefaultGroupXls() {
        return "FakePojoGroups{one='01582044000725', two='null', three='02/07/2012'}FakePojoGroups{one='71444475001600', two='null', three='02/07/2012'}";
    }

    private String getResultadoDefaultGroupExcel() {
        return "FakePojoGroups{one='ABC', two='null', three='GHI'}FakePojoGroups{one='XXX', two='null', three='XXX'}";
    }

    private String getResultadoSemRazaoExtendedGroupXls() {
        return "FakePojoGroups{one='01582044000725', two='', three='02/07/2012'}FakePojoGroups{one='71444475001600', two='AUTOMEC COMERCIAL DE VEICULOS LTDA', three='02/07/2012'}";
    }

    private String getResultadoExtendedGroupExcel() {
        return "FakePojoGroups{one='ABC', two='DEF', three='GHI'}FakePojoGroups{one='XXX', two='', three='XXX'}";
    }

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/" + name).toURI());
    }
}
