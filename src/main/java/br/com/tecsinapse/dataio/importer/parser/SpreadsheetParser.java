/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.importer.parser;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import lombok.Getter;
import lombok.Setter;

import br.com.tecsinapse.dataio.ExporterFormatter;
import br.com.tecsinapse.dataio.annotation.TableCellMapping;
import br.com.tecsinapse.dataio.converter.group.Default;
import br.com.tecsinapse.dataio.exceptions.ExporterException;
import br.com.tecsinapse.dataio.importer.ImporterUtils;
import br.com.tecsinapse.dataio.importer.Parser;
import br.com.tecsinapse.dataio.type.FileType;
import br.com.tecsinapse.dataio.util.ReflectionUtil;

public class SpreadsheetParser<T> implements Parser<T> {

    private final Class<T> clazz;
    @Setter
    private Class<?> group;
    private final InputStream inputStream;
    @Getter @Setter
    private boolean ignoreBlankLinesAtEnd = true;
    @Getter @Setter
    private boolean useFormatterToParseValueAsString = false;
    @Setter
    private int headersRows;
    @Getter @Setter
    private int sheetNumber;
    @Setter
    private boolean lastsheet = false;
    @Getter @Setter
    private ExporterFormatter exporterFormatter = ExporterFormatter.ENGLISH;
    private Workbook workbook;
    @Getter
    private final FileType fileType;

    public SpreadsheetParser(Class<T> clazz, File file) throws IOException {
        this(clazz, new FileInputStream(file), file.getName());
    }

    public SpreadsheetParser(Class<T> clazz, InputStream inputStream, String filename) {
        this(clazz, inputStream, FileType.getFileType(filename));
    }

    public SpreadsheetParser(Class<T> clazz, InputStream inputStream, FileType fileType) {
        this(clazz, inputStream, fileType, 0, false);
    }

    public SpreadsheetParser(Class<T> clazz, InputStream inputStream, FileType fileType, int headersRows, boolean lastSheet) {
        this.fileType = fileType;
        this.clazz = clazz;
        this.inputStream = new BufferedInputStream(inputStream);
        this.group = Default.class;
        setIgnoreBlankLinesAtEnd(true);
        setHeadersRows(headersRows);
        if (!lastSheet) {
            setSheetNumber(0);
        }
    }

    public void setSheetNumberAsFirstNotHidden() {
        sheetNumber = getWorkbook().getFirstVisibleTab();
    }

    @Override
    public void setFirstVisibleSheet() {
        setSheetNumber(getWorkbook().getFirstVisibleTab());
    }

    @Override
    public int getNumberOfSheets() {
        return getWorkbook().getNumberOfSheets();
    }

    @Override
    public List<T> parse() throws Exception {
        if (lastsheet) {
            setSheetNumber(getNumberOfSheets() - 1);
        }
        List<T> resultList = parseCurrentSheet();
        if (isIgnoreBlankLinesAtEnd()) {
            ImporterUtils.removeBlankLinesOfEnd(resultList, clazz);
        }
        return resultList;
    }

    private List<T> parseCurrentSheet() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        List<T> list = new ArrayList<>();
        workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt(this.sheetNumber);
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Map<Method, TableCellMapping> cellMappingByMethod = ImporterUtils.getMappedMethods(clazz, group);
        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        ReflectionUtil.setConstructorAccessible(constructor);

        int k = 0;
        for (int j = sheet.getFirstRowNum(); j <= sheet.getPhysicalNumberOfRows(); j++) {
            final Row row = sheet.getRow(j);
            k++;
            if (k <= headersRows || row == null) {
                continue;
            }
            T instance = constructor.newInstance();
            for (Entry<Method, TableCellMapping> methodTcm : cellMappingByMethod.entrySet()) {
                TableCellMapping tcm = methodTcm.getValue();
                ImporterUtils.parseSpreadsheetCell(tcm.converter(), evaluator, row.getCell(tcm.columnIndex()), methodTcm.getKey(), instance, exporterFormatter, useFormatterToParseValueAsString);
            }
            list.add(instance);
        }

        return list;
    }

    private List<List<String>> parseCurrentSheetAsStringList() {
        workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt(getSheetNumber());
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Iterator<Row> rowIterator = sheet.iterator();
        List<List<String>> list = new ArrayList<>();

        int i = 0;
        while (rowIterator.hasNext()) {
            List<String> rowList = new ArrayList<>();
            Row row = rowIterator.next();
            i++;
            if (i <= headersRows) {
                continue;
            }
            Iterator<Cell> cells = row.cellIterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                rowList.add(ImporterUtils.getValueOrEmpty(evaluator, cell, exporterFormatter));
            }
            list.add(rowList);
        }
        return list;
    }

    public List<List<String>> getLines() {
        return parseCurrentSheetAsStringList();
    }

    public Workbook getWorkbook() {
        if (workbook == null) {
            try {
                workbook = fileType.buildWorkbook(inputStream);
            } catch (Exception e) {
                throw new ExporterException(e);
            }
        }
        return workbook;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

}
