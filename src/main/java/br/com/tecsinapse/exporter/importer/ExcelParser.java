package br.com.tecsinapse.exporter.importer;

import static br.com.tecsinapse.exporter.importer.Importer.getMappedMethods;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Multimaps.filterEntries;
import static com.google.common.collect.Multimaps.transformValues;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.joda.time.LocalDate;
import org.reflections.ReflectionUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import br.com.tecsinapse.exporter.ExcelType;
import br.com.tecsinapse.exporter.ExcelUtil;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.annotation.TableCellMappings;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import br.com.tecsinapse.exporter.converter.group.Default;

public class ExcelParser<T> implements Parser<T> {

    private final Class<T> clazz;
    private File excel;
    private InputStream excelInputStream;
    private ExcelType type;
    private int initialRow;
    private final Class<?> group;

    public ExcelParser(Class<T> clazz, File file) throws IOException {
        this(clazz, file, null, ExcelType.getExcelType(file.getName()), 0, Default.class);
    }

    public ExcelParser(Class<T> clazz, File file, int initialRow) throws IOException {
        this(clazz, file, initialRow, Default.class);
    }

    public ExcelParser(Class<T> clazz, File file, int initialRow, Class<?> group) throws IOException {
        this(clazz, file, null, null, initialRow, group);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int initialRow) {
        this(clazz, inputStream, type, initialRow, Default.class);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int initialRow, Class<?> group) {
        this(clazz, null, inputStream, type, initialRow, group);
    }

    private ExcelParser(Class<T> clazz, File excel, InputStream excelInputStream, ExcelType type, int initialRow, Class<?> group) {
        checkNotNull(group);
        this.clazz = clazz;
        this.excel = excel;
        this.excelInputStream = excelInputStream;
        this.type = type;
        this.initialRow = initialRow;
        this.group = group;
    }


    /**
     * Não lê a primeira linha
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<T> parse() throws Exception {
        if (ExcelType.XLSX == type) {
            return parseXlsx();
        }
        return parseXls();
    }

    private List<T> parseXlsx() throws Exception {
        List<List<String>> xlsxLines = getXlsxLines(initialRow);

        List<T> list = new ArrayList<>();
        Map<Method, TableCellMapping> cellMappingByMethod = getMappedMethods(clazz, group);

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        for (List<String> fields : xlsxLines) {
            T instance = constructor.newInstance();

            for (Entry<Method, TableCellMapping> methodTcm : cellMappingByMethod.entrySet()) {
                TableCellMapping tcm = methodTcm.getValue();
                String value = getValueOrEmpty(fields, tcm.columnIndex());
                TableCellConverter<?> converter = tcm.converter().newInstance();
                Object obj = converter.apply(value);
                methodTcm.getKey().invoke(instance, obj);
            }
            list.add(instance);
        }
        return list;
    }

    private List<T> parseXls() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, InvalidFormatException, NoSuchMethodException {
        List<T> list = new ArrayList<>();


        Map<Method, TableCellMapping> cellMappingByMethod = getMappedMethods(clazz, group);
        Workbook wb = getWorkbook();
        Sheet sheet = wb.getSheetAt(0);

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        Iterator<Row> rowIterator = sheet.iterator();

        boolean header = true;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (header) {
                header = false;
                continue;
            }
            T instance = constructor.newInstance();

            for (Entry<Method, TableCellMapping> methodTcm : cellMappingByMethod.entrySet()) {
                TableCellMapping tcm = methodTcm.getValue();
                String value = getValueOrEmpty(row.getCell(tcm.columnIndex()));
                TableCellConverter<?> converter = tcm.converter().newInstance();
                Object obj = converter.apply(value);
                methodTcm.getKey().invoke(instance, obj);
            }
            list.add(instance);
        }
        return list;
    }

    private String getValueOrEmpty(List<String> fields, int index) {
        if (fields.isEmpty() || fields.size() <= index) {
            return "";
        }
        return fields.get(index);
    }
    
    public List<List<String>> getXlsLines() throws InvalidFormatException, IOException {
    	Workbook wb = getWorkbook();
        Sheet sheet = wb.getSheetAt(0);

        List<List<String>> lines = Lists.newArrayList();
        List<Row> linhasArquivo = Lists.newArrayList(sheet.iterator());
        for (Row row : linhasArquivo) {
			List<Cell> cells = Lists.newArrayList(row.cellIterator());
			List<String> cellsAsString = Lists.newArrayList(Collections2.transform(cells, new Function<Cell, String>() {
				@Override
				public String apply(Cell input) {
					return getValueOrEmpty(input);
				}
			}));
			lines.add(cellsAsString);
		}
        
		return lines;
    }

    private List<String> getFields(Row row) {
        List<String> values = new ArrayList<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            values.add(getValueOrEmpty(cell));
        }
        return values;
    }

    private String getValueOrEmpty(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cell.getBooleanCellValue()).toString();
            case Cell.CELL_TYPE_NUMERIC:
                CellStyle style = cell.getCellStyle();
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return new LocalDate(cell.getDateCellValue()).toString("dd/MM/yyyy");
                }
                return Double.valueOf(cell.getNumericCellValue()).toString();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_ERROR:
                return "ERRO";
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public Workbook getWorkbook() throws IOException, InvalidFormatException {
        if (excel != null) {
            return WorkbookFactory.create(excel);
        }
        return WorkbookFactory.create(excelInputStream);
    }

    public OPCPackage getOPCPackage() throws InvalidFormatException, IOException {
        if (excel != null) {
            return OPCPackage.open(excel);
        }
        return OPCPackage.open(excelInputStream);
    }
    
    public List<List<String>> getXlsxLines() throws Exception {
    	return getXlsxLines(false);
    }

    private List<List<String>> getXlsxLines(boolean ignoreFirstRow) throws Exception {

        Table table = null;
        OPCPackage container;
        container = getOPCPackage();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(container);
        XSSFReader xssfReader = new XSSFReader(container);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iter.hasNext()) {
            InputStream stream = iter.next();

            Table aba = processXlsxSheet(styles, strings, stream, ignoreFirstRow);
            if (table == null) {
                table = aba;
            } else {
                table.concatenateTableBelow(aba);
            }
            stream.close();
            //le apenas 1 aba
            break;
        }
        return table.toStringMatrix();
    }

    private List<List<String>> getXlsxLines(int initialRow) throws Exception {

        Table table = null;
        OPCPackage container;
        container = getOPCPackage();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(container);
        XSSFReader xssfReader = new XSSFReader(container);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iter.hasNext()) {
            InputStream stream = iter.next();

            Table aba = processXlsxSheet(styles, strings, stream, initialRow);
            if (table == null) {
                table = aba;
            } else {
                table.concatenateTableBelow(aba);
            }
            stream.close();
            //le apenas 1 aba
            break;
        }
        return table.toStringMatrix();
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
				while(dif-- > 0) {
					table.add("");
				}
				table.add(formattedValue);
			}

			@Override
			public void headerFooter(String text, boolean isHeader, String tagName) {

			}

		},
				false//means result instead of formula
		);
		sheetParser.setContentHandler(handler);
		sheetParser.parse(sheetSource);

		return table;
	}
}
