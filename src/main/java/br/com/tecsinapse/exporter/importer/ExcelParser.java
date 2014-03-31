package br.com.tecsinapse.exporter.importer;

import static br.com.tecsinapse.exporter.importer.ImporterXLSXType.DEFAULT;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
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
import org.reflections.ReflectionUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import br.com.tecsinapse.exporter.ExcelType;
import br.com.tecsinapse.exporter.ExcelUtil;
import br.com.tecsinapse.exporter.Table;
import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.TableCellConverter;

public class ExcelParser<T> implements Parser<T> {

    private static final int FIRST_LINE = 0;
    private static final int FIRST_SHEET = 0;
    private final Class<T> clazz;
    private final InputStream excelInputStream;
    private final ExcelType type;

    private int afterLine = Importer.DEFAULT_START_ROW;
    private int sheetNumber = 0;
    private String dateStringPattern = "dd/MM/yyyy";
    private ImporterXLSXType importerXLSXType = DEFAULT;

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
        this(clazz, new FileInputStream(file), ExcelType.getExcelType(file.getName()), afterLine, lastSheet, importerXLSXType);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type) {
        this(clazz, inputStream, type, Importer.DEFAULT_START_ROW);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine) {
		this(clazz, inputStream, type, afterLine, false, ImporterXLSXType.DEFAULT);
    }

    public ExcelParser(Class<T> clazz, InputStream inputStream, ExcelType type, int afterLine, boolean isLastSheet, ImporterXLSXType importerXLSXType) {
        this.clazz = clazz;
        this.excelInputStream = new BufferedInputStream(inputStream);
        this.type = type;
        this.afterLine = afterLine;
        this.importerXLSXType = importerXLSXType;

        if (isLastSheet) {
            if(type==ExcelType.XLSX){
                try {
                    XSSFReader xssfReader = new XSSFReader(getOPCPackage());
                    sheetNumber = Iterators.size(xssfReader.getSheetsData()) - 1;
                } catch (IOException | OpenXML4JException e) {
                    Throwables.propagate(e);
                }
            }else{
                sheetNumber = getWorkbook().getNumberOfSheets() - 1;
            }
        }
    }

    public void setDateStringPattern(String dateStringPattern) {
        this.dateStringPattern = dateStringPattern;
    }

    public void setAfterLine(int afterLine) {
        this.afterLine = afterLine;
    }

    public void setSheetNumber(int sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public int getNumberOfSheets() {
        return getWorkbook().getNumberOfSheets();
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
        List<List<String>> xlsxLines = getXlsxLines(afterLine, sheetNumber);

        List<T> list = new ArrayList<>();
		  @SuppressWarnings("unchecked")
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, 
				  ReflectionUtils.withAnnotation(TableCellMapping.class));

        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        for (List<String> fields : xlsxLines) {
			   T instance = constructor.newInstance();

            for (Method method : methods) {
                method.setAccessible(true);

                TableCellMapping tcm = method.getAnnotation(TableCellMapping.class);
                String value = getValueOrEmpty(fields, tcm.columnIndex());
                TableCellConverter<?> converter = tcm.converter().newInstance();
                Object obj = converter.apply(value);
                method.invoke(instance, obj);
            }
            list.add(instance);
        }
        return list;
    }

    private List<T> parseXls() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, InvalidFormatException, NoSuchMethodException {
        List<T> list = new ArrayList<>();
		  @SuppressWarnings("unchecked")
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, 
				  ReflectionUtils.withAnnotation(TableCellMapping.class));
        Workbook wb = getWorkbook();
        
        Sheet sheet = wb.getSheetAt(this.sheetNumber);

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

            for (Method method : methods) {
                method.setAccessible(true);

                TableCellMapping tcm = method.getAnnotation(TableCellMapping.class);
                String value = getValueOrEmpty(row.getCell(tcm.columnIndex()));
                TableCellConverter<?> converter = tcm.converter().newInstance();
                Object obj = converter.apply(value);
                method.invoke(instance, obj);
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

    public List<List<String>> getLines() throws Exception {
        if(type == ExcelType.XLSX){
            return getXlsxLines(afterLine, sheetNumber);
        }
        return getXlsLinesIncludingEmptyCells();
    }

    @Deprecated
    /**
     * @deprecated
     * Não traz na lista as ceulas que estão vazias no arquivo.
     * O método getXlsLinesIncludingEmptyCells faz esse tratamento e deve ser acessado através do método getLines.
     */
    public List<List<String>> getXlsLines() throws InvalidFormatException, IOException {
    	Workbook wb = getWorkbook();

    	Sheet sheet = wb.getSheetAt(this.sheetNumber);

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

    private List<List<String>> getXlsLinesIncludingEmptyCells() throws InvalidFormatException, IOException {
        Workbook wb = getWorkbook();
        Sheet sheet = wb.getSheetAt(this.sheetNumber);

        List<List<String>> lines = Lists.newArrayList();
        List<Row> linhasArquivo = Lists.newArrayList(sheet.iterator());
        for (int i = 0; i < linhasArquivo.size(); i++) {
            if ((i + 1) <= afterLine) {
                continue;
            }

            final Row row = linhasArquivo.get(i);

            List<String> cellsStringValues = Lists.newArrayList();
            for(int index = 0; index < row.getLastCellNum(); index++) {
                Cell cell = row.getCell(index, Row.CREATE_NULL_AS_BLANK);
                cellsStringValues.add(getValueOrEmpty(cell));
            }
            lines.add(cellsStringValues);
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
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new LocalDate(cell.getDateCellValue()).toString(dateStringPattern);
                }
                //força a tirar '.0' se for inteiro
                cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getStringCellValue();
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
				importerXLSXType.formatter,
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
}
