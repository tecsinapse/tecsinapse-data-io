/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import static br.com.tecsinapse.exporter.importer.Importer.getMappedMethods;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import br.com.tecsinapse.exporter.converter.group.Default;

public class XlsParser<T> implements Parser<T> {

    private static short DECIMAL_PRECISION = 10;

    private static LocalDate LOCAL_DATE_BIGBANG = LocalDate.fromDateFields(DateUtil.getJavaDate(0.0));

    private final Class<T> clazz;
    private final Class<?> group;
    private final InputStream excelInputStream;
    private boolean ignoreBlankLinesAtEnd = true;
    private int headersRows = Importer.DEFAULT_START_ROW;
    private int sheetNumber = 0;
    private ParserFormatter parserFormatter = ParserFormatter.DEFAULT;
    private Workbook workbook;
    public XlsParser(Class<T> clazz, File file) throws IOException {
        this(clazz, file, Default.class);
    }

    public XlsParser(Class<T> clazz, File file, Class<?> group) throws IOException {
        this(clazz, new FileInputStream(file), group);
    }

    public XlsParser(Class<T> clazz, InputStream inputStream) {
        this(clazz, inputStream, Default.class);
    }

    public XlsParser(Class<T> clazz, InputStream inputStream, Class<?> group) {
        this.clazz = clazz;
        this.excelInputStream = new BufferedInputStream(inputStream);
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
    public int getNumberOfSheets() {
        return getWorkbook().getNumberOfSheets();
    }

    @Override
    public List<T> parse() throws Exception {
        List<T> resultList = parseXls();
        if (ignoreBlankLinesAtEnd) {
            ImporterUtils.removeBlankLinesOfEnd(resultList, clazz);
        }
        return resultList;
    }

    private List<T> parseXls() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, InvalidFormatException, NoSuchMethodException {
        List<T> list = new ArrayList<>();
        workbook = getWorkbook();
        Sheet sheet = workbook.getSheetAt(this.sheetNumber);
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Map<Method, TableCellMapping> cellMappingByMethod = getMappedMethods(clazz, group);
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
                parseCell(tcm.converter(), getValueOrEmptyAsObject(evaluator, row.getCell(tcm.columnIndex())), methodTcm.getKey(), instance);
            }
            list.add(instance);
        }
        return list;
    }

    private void parseCell(Class<? extends TableCellConverter<?>> tcc, Object value, Method method, T instance) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            if (value == null) {
                method.invoke(instance, value);
            }
            Class<?> targetType = getReturnType(tcc);
            if (isInstanceOf(value, targetType)) {
                method.invoke(instance, value);
                return;
            }
            if (isInstanceOf(value, BigDecimal.class)) {
                Object numericValue = toNumericValue((BigDecimal) value, targetType);
                if (numericValue != null) {
                    method.invoke(instance, numericValue);
                    return;
                }
            }
            if (isInstanceOf(value, LocalDateTime.class)) {
                Object dateTimeValue = toDateTimeValue((LocalDateTime) value, targetType);
                if (dateTimeValue != null) {
                    method.invoke(instance, dateTimeValue);
                    return;
                }
            }
            TableCellConverter<?> converter = tcc.newInstance();
            method.invoke(instance, converter.apply(value.toString()));
        } catch (NoSuchMethodException e) {
            TableCellConverter<?> converter = tcc.newInstance();
            method.invoke(instance, converter.apply(value.toString()));
        }
    }

    private Object toNumericValue(BigDecimal bigDecimal, Class<?> targetType) {
        if (Integer.class.equals(targetType)) {
            return bigDecimal.intValue();
        }
        if (Long.class.equals(targetType)) {
            return bigDecimal.longValue();
        }
        return null;
    }

    private Object toDateTimeValue(LocalDateTime localDateTime, Class<?> targetType) {
        if (LocalDateTime.class.equals(targetType)) {
            return localDateTime;
        }
        if (LocalDate.class.equals(targetType)) {
            return localDateTime.toLocalDate();
        }
        if (LocalTime.class.equals(targetType)) {
            return localDateTime.toLocalTime();
        }
        if (String.class.equals(targetType)) {
            return formatLocalDateTimeAsString(localDateTime);
        }
        return null;
    }

    private String formatLocalDateTimeAsString(LocalDateTime localDateTime) {
        LocalTime localTime = localDateTime.toLocalTime();
        LocalDate localDate = localDateTime.toLocalDate();
        if (LocalTime.MIDNIGHT.equals(localTime)) {
            return parserFormatter.formatLocalDate(localDate);
        }

        if (LOCAL_DATE_BIGBANG.equals(localDate)) {
            return parserFormatter.formatLocalTime(localTime);
        }
        return parserFormatter.formatLocalDateTime(localDateTime);
    }

    private boolean isInstanceOf(Object value, Class<?> targetType) throws NoSuchMethodException {
        return value != null && targetType.isInstance(value);
    }

    private Class<?> getReturnType(Class<?> converter) throws NoSuchMethodException {
        Method converterMethod = converter.getMethod("apply", String.class);
        return converterMethod.getReturnType();
    }

    public List<List<String>> getLines() throws Exception {
        return getXlsLinesIncludingEmptyCells();
    }

    private List<List<String>> getXlsLinesIncludingEmptyCells() throws InvalidFormatException, IOException {
        Workbook wb = getWorkbook();
        Sheet sheet = wb.getSheetAt(this.sheetNumber);
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        List<List<String>> lines = Lists.newArrayList();
        List<Row> linhasArquivo = Lists.newArrayList(sheet.iterator());
        for (int i = 0; i < linhasArquivo.size(); i++) {
            if ((i + 1) <= headersRows) {
                continue;
            }

            final Row row = linhasArquivo.get(i);

            List<String> cellsStringValues = Lists.newArrayList();
            for (int index = 0; index < row.getLastCellNum(); index++) {
                Cell cell = row.getCell(index, Row.CREATE_NULL_AS_BLANK);
                cellsStringValues.add(getValueOrEmpty(evaluator, cell));
            }
            lines.add(cellsStringValues);
        }
        return lines;
    }

    private String getValueOrEmpty(FormulaEvaluator evaluator, Cell cell) {
        Object value = getValueOrEmptyAsObject(evaluator, cell);
        if (value instanceof LocalDateTime) {
            return formatLocalDateTimeAsString((LocalDateTime) value);
        }
        return value.toString();
    }

    private Object getValueOrEmptyAsObject(FormulaEvaluator evaluator, Cell cell) {
        final CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null) {
            return "";
        }
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cellValue.getBooleanValue());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                     LocalDateTime localDateTime = LocalDateTime.fromDateFields(cell.getDateCellValue());
                    return localDateTime;
                }
                BigDecimal bd = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
                return bd.stripTrailingZeros();
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
            case Cell.CELL_TYPE_ERROR:
                return "ERRO";
            default:
                return "";
        }
    }

    public Workbook getWorkbook() {
        if (workbook == null) {
            try {
                workbook = WorkbookFactory.create(excelInputStream);
            } catch (IOException | InvalidFormatException e) {
                throw Throwables.propagate(e);
            }
        }
        return workbook;
    }

    @Override
    public void close() throws IOException {
        excelInputStream.close();
    }

}
