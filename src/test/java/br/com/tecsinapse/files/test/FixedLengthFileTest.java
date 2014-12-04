package br.com.tecsinapse.files.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.importer.FixedLengthFileParser;
import br.com.tecsinapse.exporter.test.FakeFixedLengthFilePojo;

public class FixedLengthFileTest {

    private File getFile(String name) throws URISyntaxException {
        return new File(getClass().getResource("/files/" + name).toURI());
    }

    @DataProvider(name = "data")
    public Object[][] data() throws URISyntaxException {
        final List<FakeFixedLengthFilePojo> pojos = expectedPojos();
        return new Object[][] { { pojos, getFile("fixed-length-file.txt") } };
    }

    @DataProvider(name = "dataWithError")
    public Object[][] dataWithError() throws URISyntaxException {
        final List<FakeFixedLengthFilePojo> pojos = expectedPojos();
        return new Object[][] { { pojos, getFile("fixed-length-file-with-error.txt") } };
    }
    
    @DataProvider(name = "dataWithHeader")
    public Object[][] dataWithHeader() throws URISyntaxException{
        final List<FakeFixedLengthFilePojo> pojos = expectedPojos();
        return new Object[][] { { pojos, getFile("fixed-length-file-with-header.txt") } };
    }

    @DataProvider(name = "dataWithLongHeader")
    public Object[][] dataWithLongHeader() throws URISyntaxException{
    	final List<FakeFixedLengthFilePojo> pojos = expectedPojos();
    	return new Object[][] { { pojos, getFile("fixed-length-file-with-long-header.txt") } };
    }

    @DataProvider(name = "dataWithLongHeaderAndEOF")
    public Object[][] dataWithLongHeaderAndEOF() throws URISyntaxException{
    	final List<FakeFixedLengthFilePojo> pojos = expectedPojos();
    	return new Object[][] { { pojos, getFile("fixed-length-file-with-long-header-and-eof.txt") } };
    }
    
    private List<FakeFixedLengthFilePojo> expectedPojos() {
        final List<FakeFixedLengthFilePojo> pojos = new ArrayList<>();
        pojos.add(new FakeFixedLengthFilePojo(1, "String 01", new LocalDate(2014, 9, 19)));
        pojos.add(new FakeFixedLengthFilePojo(2, "Str02", new LocalDate(2014, 9, 20)));
        pojos.add(new FakeFixedLengthFilePojo(3, "0303030303", new LocalDate(2014, 9, 21)));
        pojos.add(new FakeFixedLengthFilePojo(4, "Acentuação", new LocalDate(2014, 9, 21)));
        return pojos;
    }

    @Test(dataProvider = "data")
    public void validaImportacao(List<FakeFixedLengthFilePojo> pojos, File file) throws IOException,
            ReflectiveOperationException {
        List<FakeFixedLengthFilePojo> importedPojos = new FixedLengthFileParser<FakeFixedLengthFilePojo>(
                FakeFixedLengthFilePojo.class).withCharset(Charset.forName("UTF-8")).parse(file);

        validaImportacao(pojos, importedPojos);
    }
    
    @Test(dataProvider = "dataWithHeader")
    public void validarImpotacaoComCabecalho(List<FakeFixedLengthFilePojo> pojos, File file) throws IOException, ReflectiveOperationException{

        List<FakeFixedLengthFilePojo> importedPojos = new FixedLengthFileParser<FakeFixedLengthFilePojo>(
                FakeFixedLengthFilePojo.class).withIgnoreFirstLine(true).withCharset(Charset.forName("UTF-8")).parse(file);

        validaImportacao(pojos, importedPojos);
    }

    @Test(dataProvider = "dataWithLongHeader")
    public void validarImpotacaoComCabecalhoLongo(List<FakeFixedLengthFilePojo> pojos, File file) throws IOException, ReflectiveOperationException{
    	
    	List<FakeFixedLengthFilePojo> importedPojos = new FixedLengthFileParser<FakeFixedLengthFilePojo>(
    			FakeFixedLengthFilePojo.class, 3).withIgnoreFirstLine(true).withCharset(Charset.forName("UTF-8")).parse(file);
    	
    	validaImportacao(pojos, importedPojos);
    }

    @Test(dataProvider = "dataWithLongHeaderAndEOF")
    public void validarImpotacaoComCabecalhoLongoEFimDeArquivo(List<FakeFixedLengthFilePojo> pojos, File file) throws IOException, ReflectiveOperationException{
    	final String eof = "";
    	List<FakeFixedLengthFilePojo> importedPojos = new FixedLengthFileParser<FakeFixedLengthFilePojo>(
    			FakeFixedLengthFilePojo.class, 3).withIgnoreFirstLine(true).withEOFCharacter(eof).withCharset(Charset.forName("UTF-8")).parse(file);
    	
    	validaImportacao(pojos, importedPojos);
    }
    

    @Test(dataProvider = "dataWithLongHeaderAndEOF", expectedExceptions=IllegalArgumentException.class)
    public void validarImpotacaoComCabecalhoLongoEFimDeArquivoErro(List<FakeFixedLengthFilePojo> pojos, File file) throws IOException, ReflectiveOperationException{
    	final String eof = "-";
    	List<FakeFixedLengthFilePojo> importedPojos = new FixedLengthFileParser<FakeFixedLengthFilePojo>(
    			FakeFixedLengthFilePojo.class, 3).withIgnoreFirstLine(true).withEOFCharacter(eof).withCharset(Charset.forName("UTF-8")).parse(file);
    	
    	validaImportacao(pojos, importedPojos);
    }
    

    @Test(dataProvider = "dataWithError", expectedExceptions = IllegalArgumentException.class)
    public void validaImportacaoErro(List<FakeFixedLengthFilePojo> pojos, File file) throws IOException,
            ReflectiveOperationException {
        new FixedLengthFileParser<FakeFixedLengthFilePojo>(FakeFixedLengthFilePojo.class).withCharset(
                Charset.forName("UTF-8")).parse(file);
    }

    @Test(dataProvider = "dataWithError")
    public void validaImportacaoIgnoreErros(List<FakeFixedLengthFilePojo> pojos, File file) throws IOException,
            ReflectiveOperationException {
        List<FakeFixedLengthFilePojo> importedPojos = new FixedLengthFileParser<FakeFixedLengthFilePojo>(
                FakeFixedLengthFilePojo.class).withCharset(Charset.forName("UTF-8")).withIgnoreLineWhenErrors(true)
                .parse(file);

        validaImportacao(pojos, importedPojos);
    }

	private void validaImportacao(List<FakeFixedLengthFilePojo> pojos,
			List<FakeFixedLengthFilePojo> importedPojos) {
		Assert.assertEquals(importedPojos.size(), pojos.size());

        for (int i = 0; i < pojos.size(); i++) {
            Assert.assertEquals(importedPojos.get(i), pojos.get(i));
        }
	}
}
