/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import static br.com.tecsinapse.exporter.importer.Importer.getMappedMethods;
import static br.com.tecsinapse.exporter.importer.ImporterXLSXType.DEFAULT;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import br.com.tecsinapse.exporter.ExcelType;
import br.com.tecsinapse.exporter.ExcelUtil;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import br.com.tecsinapse.exporter.converter.group.Default;

public class ExcelParser<T> implements Parser<T> {

    private static final int FIRST_LINE = 0;
    private static final int FIRST_SHEET = 0;
    private final Class<T> clazz;
    private final InputStream excelInputStream;
    private final ExcelType type;
    private final Class<?> group;

    private int afterLine = Importer.DEFAULT_START_ROW;
    private int sheetNumber = 0;
    private String dateStringPattern;
    private String dateTimeStringPattern;
    private ImporterXLSXType importerXLSXType = DEFAULT;
    private boolean dateAsLocalDateTime = false;
    private boolean dateAsString = true;

    //lazy somente criado ao chamar getWorkbook
    private Workbook workbook;
    private OPCPackage opcPackage;

    public ExcelParser(Class<T> clazz, File file) throws IOException {
        this(clazz, new FileInputStream(file), ExcelType.getExcelType(file.getName()));
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine) throws IOException {
        this(clazz, new FileInputStream(file), ExcelType.getExcelType(file.getName()), afterLine);
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine, boolean lastSheet, ImporterXLSXType importerXLSXType) throws IOException {
        this(clazz, file, afterLine, lastSheet, importerXLSXType, Default.class);
    }

    public ExcelParser(Class<T> clazz, File file, int afterLine, boolean lastSheet, ImporterXLSXType importerXLSXType, Class<?> group) throws IOException {
        this(clazz, new FileInputStream(file), ExcelType.getExcelType(file.getName()), afterLine, lastSheet, importerXLSXType, group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type) {
        this(clazz, inputStream, type, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, Class<?> group) {
        this(clazz, inputStream, type, Importer.DEFAULT_START_ROW, group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine) {
        this(clazz, inputStream, type, afterLine, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine, Class<?> group) {
        this(clazz, inputStream, type, afterLine, false, ImporterXLSXType.DEFAULT, group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine, boolean isLastSheet, ImporterXLSXType importerXLSXType) {
        this(clazz, inputStream, type, afterLine, isLastSheet, importerXLSXType, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine, boolean isLastSheet, ImporterXLSXType importerXLSXType, Class<?> group) {
        this.clazz = clazz;
        this.excelInputStream = new BufferedInputStream(inputStream);
        this.type = type;
        this.dateStringPattern = type.getDefaultDatePattern();
        this.dateTimeStringPattern = type.getDefaultDateTimePattern();
        this.afterLine = afterLine;
        this.importerXLSXType = importerXLSXType;
        this.group = group;

        if (isLastSheet) {
            sheetNumber = getNumberOfSheets() - 1;
        }
    }

    public String getDateStringPattern() {
        return dateStringPattern;
    }

    @Override
    public void setDateStringPattern(String dateStringPattern) {
        this.dateStringPattern = dateStringPattern;
    }

    @Override
    public void setDateTimeStringPattern(String dateTimeStringPattern) {
        this.dateTimeStringPattern = dateTimeStringPattern;
    }

    public void setAfterLine(int afterLine) {
        this.afterLine = afterLine;
    }

    public void setSheetNumber(int sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public int getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumberAsFirstNotHidden() {
        if (type == ExcelType.XLSX) {
            try {
                sheetNumber = new XSSFWorkbook(getOPCPackage()).getFirstVisibleTab();
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        } else {
            sheetNumber = getWorkbook().getFirstVisibleTab();
        }
    }

    @Override
    public int getNumberOfSheets() {
        if (type == ExcelType.XLSX) {
            try {
                XSSFReader xssfReader = new XSSFReader(getOPCPackage());
                return Iterators.size(xssfReader.getSheetsData());
            } catch (IOException | OpenXML4JException e) {
                throw Throwables.propagate(e);
            }
        } else {
            return getWorkbook().getNumberOfSheets();
        }
    }

    /**
     * Não lê a primeira linha
     *
     * @return lista represetando as linhas da planilha
     * @throws Exception em caso de erro
     */
    @Override
    public List<T> parse() throws Exception {
        List<T> resultList;
        if (ExcelType.XLSX == type) {
            resultList = parseXlsx();
        } else {
            resultList = parseXls();
        }
        removeBlankLinesOfEnd(resultList);
        return resultList;
    }

    private void removeBlankLinesOfEnd(List<T> resultList) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
        Collections.reverse(resultList);
        final PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        final Set<Method> readMethodsOfWriteMethodsWithTableCellMapping = FluentIterable.from(Arrays.asList(propertyDescriptors))
                .filter(hasWriteAndReadMethod())
                .filter(hasAnnotationTableCellMapping())
                .transform(toReadMethod())
                .toSet();

        if (!readMethodsOfWriteMethodsWithTableCellMapping.isEmpty()) {
            final Iterator<T> iterator = resultList.iterator();
            while (iterator.hasNext()) {
                final T instance = iterator.next();
                if (allPropertiesHasNoValue(instance, readMethodsOfWriteMethodsWithTableCellMapping)) {
                    iterator.remove();
                } else {
                    break;
                }
            }
        }
        Collections.reverse(resultList);
    }

    private static Function<PropertyDescriptor, Method> toReadMethod() {
        return new Function<PropertyDescriptor, Method>() {
            @Override
            public Method apply(PropertyDescriptor propertyDescriptor) {
                return propertyDescriptor.getReadMethod();
            }
        };
    }

    private boolean allPropertiesHasNoValue(T instance, Set<Method> readMethodsOfWriteMethodsWithTableCellMapping) throws InvocationTargetException, IllegalAccessException {
        for (Method method : readMethodsOfWriteMethodsWithTableCellMapping) {
            final Object value = method.invoke(instance);
            if (method.getReturnType().equals(String.class)) {
                String valueStr = nullToEmpty((String) value).trim();
                if (!isNullOrEmpty(valueStr)) {
                    return false;
                }
            } else if (value != null) {
                return false;
            }
        }
        return true;
    }

    private Predicate<PropertyDescriptor> hasAnnotationTableCellMapping() {
        return new Predicate<PropertyDescriptor>() {
            @Override
            public boolean apply(PropertyDescriptor propertyDescriptor) {
                final Method writeMethod = propertyDescriptor.getWriteMethod();
                for (Annotation annotation : writeMethod.getDeclaredAnnotations()) {
                    if (annotation instanceof TableCellMapping) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private Predicate<PropertyDescriptor> hasWriteAndReadMethod() {
        return new Predicate<PropertyDescriptor>() {
            @Override
            public boolean apply(PropertyDescriptor propertyDescriptor) {
                return propertyDescriptor.getWriteMethod() != null &&
                        propertyDescriptor.getReadMethod() != null;
            }
        };
    }

    private List<T> parseXlsx() throws Exception {
        List<List<String>> xlsxLines = getXlsxLines(afterLine, sheetNumber);

        List<T> list = new ArrayList<>();
        Map<Method, TableCellMapping> cellMappingByMethod = getMappedMethods(clazz, group);

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        for (List<String> fields : xlsxLines) {
            T instance = constructor.newInstance();

            for (Entry<Method, TableCellMapping> methodTcm : cellMappingByMethod.entrySet()) {

                Method method = methodTcm.getKey();
                method.setAccessible(true);

                TableCellMapping tcm = methodTcm.getValue();

                String value = getValueOrEmpty(fields, tcm.columnIndex());

                try {
                    T obj;
                    if (Strings.isNullOrEmpty(value)) {
                        obj = null;
                    } if (isReturnType(tcm.converter(), LocalDate.class)) {
                        LocalDateTime localDateTime = DateTimeFormat.forPattern(getDateStringPattern()).parseLocalDateTime(value);
                        obj = (T) localDateTime.toLocalDate();
                    } else if (isReturnType(tcm.converter(), LocalDateTime.class)) {
                        obj = (T) DateTimeFormat.forPattern(getDateStringPattern()).parseLocalDateTime(value);
                    } else {
                        TableCellConverter converter = tcm.converter().newInstance();
                        obj = (T) converter.apply(value);
                    }
                    method.invoke(instance, obj);

                } catch (NoSuchMethodException e) {
                    TableCellConverter<?> converter = tcm.converter().newInstance();
                    method.invoke(instance, converter.apply(value));
                    e.printStackTrace();
                }
            }
            list.add(instance);
        }
        return list;
    }

    private List<T> parseXls() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, InvalidFormatException, NoSuchMethodException {
        List<T> list = new ArrayList<>();

        Map<Method, TableCellMapping> cellMappingByMethod = getMappedMethods(clazz, group);
        Workbook wb = getWorkbook();
        Sheet sheet = wb.getSheetAt(this.sheetNumber);

        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        Iterator<Row> rowIterator = sheet.iterator();

        int i = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if ((i + 1) <= afterLine) {
                i++;
                continue;
            }
            T instance = constructor.newInstance();

            for (Entry<Method, TableCellMapping> methodTcm : cellMappingByMethod.entrySet()) {

                TableCellMapping tcm = methodTcm.getValue();
                Method method = methodTcm.getKey();
                method.setAccessible(true);

                Object value = getValueOrEmptyAsObject(evaluator, row.getCell(tcm.columnIndex()));
                try {
                    if (!dateAsString && isSameType(value, tcm.converter())) {
                        method.invoke(instance, value);
                    } else {
                        TableCellConverter<?> converter = tcm.converter().newInstance();
                        method.invoke(instance, converter.apply(value.toString()));
                    }
                } catch (NoSuchMethodException e) {
                    TableCellConverter<?> converter = tcm.converter().newInstance();
                    method.invoke(instance, converter.apply(value.toString()));
                }
            }
            list.add(instance);
        }
        return list;
    }

    private boolean isSameType(Object value, Class<?> converter) throws NoSuchMethodException {
        Method converterMethod = converter.getMethod("apply", String.class);
        Class<?> returnType = converterMethod.getReturnType();
        boolean isInstance = value != null && returnType.isInstance(value);
        return isInstance;
    }

    private boolean isReturnType(Class<?> converter, Class<?> type) throws NoSuchMethodException {
        Method converterMethod = converter.getMethod("apply", String.class);
        Class<?> returnType = converterMethod.getReturnType();
        return returnType != null && returnType.equals(type);
    }

    private String getValueOrEmpty(List<String> fields, int index) {
        if (fields.isEmpty() || fields.size() <= index) {
            return "";
        }
        return fields.get(index);
    }

    public List<List<String>> getLines() throws Exception {
        if (type == ExcelType.XLSX) {
            return getXlsxLines(afterLine, sheetNumber);
        }
        return getXlsLinesIncludingEmptyCells();
    }


    /**
     * @return lista representando as linhas da planilha
     * @see #getXlsLinesIncludingEmptyCells()
     * @deprecated Não traz na lista as ceulas que estão vazias no arquivo.
     * O método getXlsLinesIncludingEmptyCells faz esse tratamento e deve ser acessado através do método getLines.
     */
    @Deprecated
    public List<List<String>> getXlsLines() {
        Workbook wb = getWorkbook();

        Sheet sheet = wb.getSheetAt(this.sheetNumber);
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        List<List<String>> lines = Lists.newArrayList();
        List<Row> linhasArquivo = Lists.newArrayList(sheet.iterator());
        for (Row row : linhasArquivo) {
            List<Cell> cells = Lists.newArrayList(row.cellIterator());
            List<String> cellsAsString = Lists.newArrayList(Collections2.transform(cells, new Function<Cell, String>() {
                @Override
                public String apply(Cell input) {
                    return getValueOrEmpty(evaluator, input);
                }
            }));
            lines.add(cellsAsString);
        }

        return lines;
    }

    private List<List<String>> getXlsLinesIncludingEmptyCells() throws InvalidFormatException, IOException {
        Workbook wb = getWorkbook();
        Sheet sheet = wb.getSheetAt(this.sheetNumber);
        final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        List<List<String>> lines = Lists.newArrayList();
        List<Row> linhasArquivo = Lists.newArrayList(sheet.iterator());
        for (int i = 0; i < linhasArquivo.size(); i++) {
            if ((i + 1) <= afterLine) {
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
        return getValueOrEmptyAsObject(evaluator, cell).toString();
    }

    private Object getValueOrEmptyAsObject(FormulaEvaluator evaluator, Cell cell) {
        final CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null) {
            return "";
        }
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cellValue.getBooleanValue()).toString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    if (dateAsLocalDateTime) {
                        LocalDateTime localDateTime = LocalDateTime.fromDateFields(cell.getDateCellValue());
                        return dateAsString ? localDateTime.toString(dateTimeStringPattern) : localDateTime;
                    }
                    LocalDate localDate = new LocalDate(cell.getDateCellValue());
                    return dateAsString ? localDate.toString(dateStringPattern) : localDate;
                }
                //força a tirar '.0' se for inteiro
                cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_STRING:
                return cellValue.getStringValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_ERROR:
                return "ERRO";
            case Cell.CELL_TYPE_FORMULA://nunca será formula após evaluator
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

    @Deprecated
    /**
     * @deprecated metodo especifico para xlsx não é api ts-expoter pública, será alterado para privado ou removido em futuras versões
     */
    public OPCPackage getOPCPackage() {
        //não delegar para getWorkbook().getPackage() arquivo muito grande(33mb) lançariam OOME
        if (opcPackage == null) {
            try {
                opcPackage = OPCPackage.open(excelInputStream);
            } catch (IOException | InvalidFormatException e) {
                throw Throwables.propagate(e);
            }
        }
        return opcPackage;
    }

    /**
     * @return lista contendo as linhas da planilha
     * @throws Exception em caso de erro
     * @see #getLines()
     * @deprecated Use getLines() mais afterLine 0 nao deveria importar se e xls ou xlsx
     */
    @Deprecated
    public List<List<String>> getXlsxLines() throws Exception {
        return getXlsxLines(FIRST_LINE, FIRST_SHEET);
    }

    private List<List<String>> getXlsxLines(int initialRow, int sheet) throws Exception {

        Table table = null;
        OPCPackage container;
        container = getOPCPackage();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(container);
        XSSFReader xssfReader = new XSSFReader(container);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        int currentSheet = -1;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            currentSheet++;
            if (currentSheet != sheet) {
                continue;
            }

            table = processXlsxSheet(styles, strings, stream, initialRow);

            stream.close();
            //le apenas 1 aba
            break;
        }

        return table != null ? table.toStringMatrix() : null;
    }

    protected Table processXlsxSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream, boolean ignoreFirstRow) throws Exception {

        final Table table = processXlsxSheet(styles, strings, sheetInputStream);

        if (ignoreFirstRow) {
            table.removeFirstRow();
        }

        return table;
    }


    protected Table processXlsxSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream, int rowInitial) throws Exception {

        final Table table = processXlsxSheet(styles, strings, sheetInputStream);

        table.removeInitialRows(rowInitial);

        return table;
    }

    private Table processXlsxSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream) throws Exception {

        final Table table = new Table();
        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();

        ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, new XSSFSheetXMLHandler.SheetContentsHandler() {

            @Override
            public void startRow(int rowNum) {
                table.addNewRow();
            }

            @Override
            public void endRow() {
            }

            @Override
            public void cell(String cellReference, String formattedValue) {
                int columnIndex = ExcelUtil.getColumnIndexByColumnName(cellReference);
                int idx = table.getNextColumnIndexOfLastRow();
                int dif = columnIndex - idx;
                while (dif-- > 0) {
                    table.add("");
                }
                table.add(formattedValue);
            }

            @Override
            public void headerFooter(String text, boolean isHeader, String tagName) {

            }

        },
                importerXLSXType.getFormatter(this),
                false//means result instead of formula
        );
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);

        return table;
    }

    @Override
    public void close() throws IOException {
        excelInputStream.close();
    }

    public void setDateAsLocalDateTime(boolean dateAsLocalDateTime) {
        this.dateAsLocalDateTime = dateAsLocalDateTime;
    }

    public void setDateAsString(boolean dateAsString) {
        this.dateAsString = dateAsString;
    }

    public boolean isDateAsString() {
        return dateAsString;
    }
}
