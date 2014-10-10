package br.com.tecsinapse.exporter.test;

import br.com.tecsinapse.exporter.converter.group.Default;
import br.com.tecsinapse.exporter.importer.Importer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class ImporterTest {



    @DataProvider(name = "arquivos")
    public Object[][] getExcel() throws URISyntaxException {

        return new Object[][]{

                //TODO Implementar testes com custom groups
                {getFile("teste_sem_razao.xlsx"), getResultadoTesteSemRazaoXlsx(), Default.class},
//                {getFile("teste_sem_razao.xlsx"), getResultadoTesteSemRazaoXlsx(), TestGroup.class},
//                {getFile("teste_sem_razao.xlsx"), getResultadoTesteSemRazaoXlsx(), TestDefaultExtendedGroup.class},
                {getFile("excel.xls"), getResultadoExcel(), Default.class},
//                {getFile("excel.xls"), getResultadoExcel(), TestGroup.class},
//                {getFile("excel.xls"), getResultadoExcel(), TestDefaultExtendedGroup.class},
                {getFile("excel.xlsx"), getResultadoExcel(), Default.class},
//                {getFile("excel.xlsx"), getResultadoExcel(), TestGroup.class},
//                {getFile("excel.xlsx"), getResultadoExcel(), TestDefaultExtendedGroup.class}

                    //TODO Verificar onde está esse arquivo "excel.csv
//                {getFile("excel.csv"), getResultadoExcel(), Default.class},
//                {getFile("excel.csv"), getResultadoExcel(), TestGroup.class},
//                {getFile("excel.csv"), getResultadoExcel(), TestDefaultExtendedGroup.class},
        };
    }

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/" + name).toURI());
    }

    @Test(dataProvider = "arquivos")
    public void test(File file, String result, Class<?> group) throws Exception {
        List<FakePojo> pojos = new Importer<>(FakePojo.class, file, group).parse();
        StringBuilder sb = new StringBuilder();
        for (FakePojo fp : pojos) {
            sb.append(fp);
        }
        Assert.assertEquals(sb.toString(), result);
    }

    private String getResultadoExcel() {
        return "FakePojo{one='ABC', two='DEF', three='GHI'}FakePojo{one='XXX', two='', three='XXX'}";
    }

    private String getResultadoTesteSemRazaoXlsx() {
        return "FakePojo{one='01582044000725', two='', three='02/07/2012'}FakePojo{one='71444475001600', two='AUTOMEC COMERCIAL DE VEICULOS LTDA', three='02/07/2012'}";
    }
}
