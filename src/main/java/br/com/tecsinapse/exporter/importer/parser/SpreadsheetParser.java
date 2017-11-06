/*
 * Tecsinapse Data Input and Output
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Throwables;

import br.com.tecsinapse.exporter.ExporterFormatter;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.group.Default;
import br.com.tecsinapse.exporter.importer.ImporterUtils;
import br.com.tecsinapse.exporter.importer.ImporterXLSXType;
import br.com.tecsinapse.exporter.importer.Parser;
import br.com.tecsinapse.exporter.type.FileType;

public class SpreadsheetParser<T> implements Parser<T> {

    private final Class<T> clazz;
    private Class<?> group;
    private final InputStream inputStream;
    private boolean ignoreBlankLinesAtEnd = true;
    private boolean useFormatterToParseValueAsString = false;
    private int headersRows;
    private int sheetNumber;
    private boolean lastsheet = false;
    private ExporterFormatter exporterFormatter = ExporterFormatter.ENGLISH;
    private Workbook workbook;
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

    public boolean isIgnoreBlankLinesAtEnd() {
        return ignoreBlankLinesAtEnd;
    }

    public void setIgnoreBlankLinesAtEnd(boolean ignoreBlankLinesAtEnd) {
        this.ignoreBlankLinesAtEnd = ignoreBlankLinesAtEnd;
    }

    public ExporterFormatter getExporterFormatter() {
        return exporterFormatter;
    }

    public void setExporterFormatter(ExporterFormatter exporterFormatter) {
        this.exporterFormatter = exporterFormatter;
    }

    public void setHeadersRows(int headersRows) {
        this.headersRows = headersRows;
    }

    /**
     * For compatibility
     *
     * @deprecated use {@link SpreadsheetParser#setHeadersRows(int)} }
     */
    @Deprecated
    public void setAfterLine(int headersRows) {
        setHeadersRows(headersRows);
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

    public boolean isUseFormatterToParseValueAsString() {
        return useFormatterToParseValueAsString;
    }

    public void setUseFormatterToParseValueAsString(boolean useFormatterToParseValueAsString) {
        this.useFormatterToParseValueAsString = useFormatterToParseValueAsString;
    }

    @Override
    public void setLastsheet(boolean lastsheet) {
        this.lastsheet = lastsheet;
    }

    @Override
    public void setFirstVisibleSheet() {
        setSheetNumber(getWorkbook().getFirstVisibleTab());
    }

    @Override
    public void setGroup(Class<?> group) {
        this.group = group;
    }

    @Override
    public FileType getFileType() {
        return fileType;
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
        constructor.setAccessible(true);

        int i = 0;
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            i++;
            if (i <= headersRows) {
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

    public List<List<String>> getLines() throws Exception {
        return parseCurrentSheetAsStringList();
    }

    public Workbook getWorkbook() {
        if (workbook == null) {
            try {
                workbook = fileType.buildWorkbook(inputStream);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
        return workbook;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    /**
     * For compatibility
     *
     * @deprecated
     */
    @Deprecated
    public SpreadsheetParser(Class<T> clazz, InputStream inputStream, FileType fileType, int headersRows, boolean lastSheet, ImporterXLSXType importerXLSXType) {
        this(clazz, inputStream, fileType, headersRows, lastSheet);
    }

}
