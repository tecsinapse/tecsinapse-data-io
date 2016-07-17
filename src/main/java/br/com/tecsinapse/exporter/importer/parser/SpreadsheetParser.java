/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Throwables;

import br.com.tecsinapse.exporter.ImporterType;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.group.Default;
import br.com.tecsinapse.exporter.importer.Importer;
import br.com.tecsinapse.exporter.importer.ImporterUtils;
import br.com.tecsinapse.exporter.importer.Parser;
import br.com.tecsinapse.exporter.importer.ParserFormatter;

public class SpreadsheetParser<T> implements Parser<T> {

    private final Class<T> clazz;
    private final Class<?> group;
    private final InputStream inputStream;
    private boolean ignoreBlankLinesAtEnd = true;
    private int headersRows = Importer.DEFAULT_START_ROW;
    private int sheetNumber = 0;
    private ParserFormatter parserFormatter = ParserFormatter.DEFAULT;
    private Workbook workbook;
    private final ImporterType importerType;
    public SpreadsheetParser(Class<T> clazz, File file) throws IOException {
        this(clazz, file, Default.class);
    }

    public SpreadsheetParser(Class<T> clazz, File file, Class<?> group) throws IOException {
        this(clazz, new FileInputStream(file), group, ImporterType.getImporterType(file.getName()));
    }

    public SpreadsheetParser(Class<T> clazz, InputStream inputStream, Class<?> group, ImporterType importerType) {
        this.importerType = importerType;
        this.clazz = clazz;
        this.inputStream = new BufferedInputStream(inputStream);
        this.group = group;
    }

    public boolean isIgnoreBlankLinesAtEnd() {
        return ignoreBlankLinesAtEnd;
    }

    public void setIgnoreBlankLinesAtEnd(boolean ignoreBlankLinesAtEnd) {
        this.ignoreBlankLinesAtEnd = ignoreBlankLinesAtEnd;
    }

    public ParserFormatter getParserFormatter() {
        return parserFormatter;
    }

    public void setParserFormatter(ParserFormatter parserFormatter) {
        this.parserFormatter = parserFormatter;
    }

    public void setHeadersRows(int headersRows) {
        this.headersRows = headersRows;
    }

    public int getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumber(int sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public void setSheetNumberAsFirstNotHidden() {
        sheetNumber = getWorkbook().getFirstVisibleTab();
    }

    @Override
    public ImporterType getImporterType() {
        return importerType;
    }

    @Override
    public int getNumberOfSheets() {
        return getWorkbook().getNumberOfSheets();
    }

    @Override
    public List<T> parse() throws Exception {
        List<T> resultList = parseCurrentSheet();
        if (ignoreBlankLinesAtEnd) {
            ImporterUtils.removeBlankLinesOfEnd(resultList, clazz);
        }
        return resultList;
    }

    private List<T> parseCurrentSheet() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, InvalidFormatException, NoSuchMethodException {
        List<T> list = new ArrayList<>();
        workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt(this.sheetNumber);
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Map<Method, TableCellMapping> cellMappingByMethod = ImporterUtils.getMappedMethods(clazz, group);
        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        int i = 0;
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if ((i + 1) <= headersRows) {
                i++;
                continue;
            }
            T instance = constructor.newInstance();
            for (Entry<Method, TableCellMapping> methodTcm : cellMappingByMethod.entrySet()) {
                TableCellMapping tcm = methodTcm.getValue();
                ImporterUtils.parseSpreadsheetCell(tcm.converter(), evaluator, row.getCell(tcm.columnIndex()), methodTcm.getKey(), instance, parserFormatter);
            }
            list.add(instance);
        }
        return list;
    }

    private List<List<String>> parseCurrentSheetAsStringList() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, InvalidFormatException, NoSuchMethodException {
        workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt(this.sheetNumber);
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        int i = 0;
        Iterator<Row> rowIterator = sheet.iterator();
        List<List<String>> list = new ArrayList<>();
        while (rowIterator.hasNext()) {
            List<String> rowList = new ArrayList<>();
            Row row = rowIterator.next();
            if ((i + 1) <= headersRows) {
                i++;
                continue;
            }
            Iterator<Cell> cells = row.cellIterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                rowList.add(ImporterUtils.getValueOrEmpty(evaluator, cell, parserFormatter));
            }
            list.add(rowList);
        }
        return list;
    }

    public List<List<String>> getLines() throws Exception {
        return parseCurrentSheetAsStringList();
    }

    public Workbook getWorkbook() {
        if (workbook == null) {
            try {
                workbook = importerType.buildWorkbook(inputStream);
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
        return workbook;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

}
