/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.datasources;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.Assert;

import br.com.tecsinapse.exporter.importer.Parser;
import br.com.tecsinapse.exporter.type.FileType;

public class FileDataParser<T extends ApplyTest<T>> implements ApplyTest {

    private final Map<Integer, List<T>> sheets = new HashMap<>();
    private List<T> currentSheet;
    private final File file;
    private Parser<T> parser;
    private final FileType expectedFileType;


    public FileDataParser(File file, FileType expectedFileType) {
        this.file = file;
        this.expectedFileType = expectedFileType;
    }

    public void setParser(Parser<T> parser) {
        this.parser = parser;
    }

    public File getFile() {
        return file;
    }

    public FileDataParser newSheet() {
        currentSheet = new ArrayList<T>();
        sheets.put(sheets.size() - 1, currentSheet);
        return this;
    }

    public FileDataParser addRow(T data) {
        currentSheet.add(data);
        return this;
    }

    public FileType getExpectedFileType() {
        return expectedFileType;
    }

    public Map<Integer, List<T>> getSheets() {
        return sheets;
    }

    @Override
    public void test() throws Exception {
        String identifier = file.getName();
        Assert.assertEquals(parser.getNumberOfSheets(), sheets.size(), identifier);
        for (Entry<Integer, List<T>> entry : sheets.entrySet()) {
            parser.setSheetNumber(entry.getKey() + 1);
            List<T> actualParsedSheet = parser.parse();
            currentSheet = entry.getValue();
            Assert.assertEquals(actualParsedSheet.size(), currentSheet.size(), identifier);
            for (int i = 0; i < actualParsedSheet.size(); i++) {
                T actual = actualParsedSheet.get(i);
                T expected = currentSheet.get(i);
                expected.setActual(actual, identifier);
            }
        }
    }

    @Override
    public void setActual(Object actual, String identifier) {

    }

    @Override
    public String toString() {
        String str = file != null ? file.getName() : super.toString();
        return str;
    }
}
