/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.datasources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.testng.annotations.DataProvider;

import br.com.tecsinapse.ResourceFiles;
import br.com.tecsinapse.dataio.ExporterFormatter;
import br.com.tecsinapse.dataio.util.Constants;

public class SpreadsheetDs {

    protected static final List<Locale> locales = Arrays.asList(Locale.ENGLISH, Constants.LOCALE_PT_BR, Constants.LOCALE_ES_ES, Locale.FRANCE, Locale.GERMANY);
    protected static final ExporterFormatter exporterFormatterEspanol = new ExporterFormatter(Constants.LOCALE_ES_ES);

    @DataProvider(name = "spreadsheetDs")
    public Object[][] excelParserDs() {
        List<Object> listObjects = getFileWithLocales(ResourceFiles.EXCEL_WITH_EMPTY_LINES_XLS);
        listObjects.addAll(getFileWithLocales(ResourceFiles.EXCEL_WITH_EMPTY_LINES_XLSX));
        Object[][] objects = listObjects.toArray(new Object[][] {});
        return objects;
    }

    protected List<Object> getFileWithLocales(ResourceFiles resourceFiles) {
        return getFileWithLocales(resourceFiles, locales);
    }

    protected List<Object> getFileWithLocales(ResourceFiles resourceFiles, List<Locale> withLocale) {
        FileDataParser<DataParser> fileDataParser = resourceFiles.getFileDataParser();
        List<Object> objects = new ArrayList<>();
        for(Locale locale : withLocale) {
            objects.add(new Object[]{fileDataParser, ExporterFormatter.BRAZILIAN, locale});
            objects.add(new Object[]{fileDataParser, ExporterFormatter.ENGLISH, locale});
            objects.add(new Object[]{fileDataParser, exporterFormatterEspanol, locale});
        }
        return objects;
    }

}
