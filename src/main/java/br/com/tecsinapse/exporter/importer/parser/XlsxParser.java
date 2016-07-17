/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer.parser;

public class XlsxParser<T> {
/*

    private final Class<T> clazz;
    private final Class<?> group;
    private final InputStream inputStream;
    private boolean ignoreBlankLinesAtEnd = true;
    private int headersRows = Importer.DEFAULT_START_ROW;
    private int sheetNumber = 0;
    private ParserFormatter parserFormatter = ParserFormatter.DEFAULT;
    private XSSFWorkbook xssfWorkbook;

    public XlsxParser(Class<T> clazz, File file) throws IOException {
        this(clazz, file, Default.class);
    }

    public XlsxParser(Class<T> clazz, File file, Class<?> group) throws IOException {
        this(clazz, new FileInputStream(file), group);
    }

    public XlsxParser(Class<T> clazz, InputStream inputStream) {
        this(clazz, inputStream, Default.class);
    }

    public XlsxParser(Class<T> clazz, InputStream inputStream, Class<?> group) {
        this.clazz = clazz;
        this.inputStream = new BufferedInputStream(inputStream);
        this.group = group;
    }

    private List<List<String>> getXlsxLines(int sheet) throws IOException, SAXException, OpenXML4JException, ParserConfigurationException {
        return null;

        */
/*Table table = null;
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
            table = processXlsxSheet(styles, strings, stream, headersRows);
            stream.close();
            break;
        }

        return table != null ? table.toStringMatrix() : Collections.<List<String>>emptyList();*//*

    }

    private Table processXlsxSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream, int rowInitial) throws ParserConfigurationException, SAXException, IOException {
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
            public void endRow(int i) {

            }

            @Override
            public void cell(String s, String s1, XSSFComment xssfComment) {
                int columnIndex = ExcelUtil.getColumnIndexByColumnName(s);
                int idx = table.getNextColumnIndexOfLastRow();
                int dif = columnIndex - idx;
                while (dif-- > 0) {
                    table.add("");
                }
                table.add(s1);
            }

            @Override
            public void headerFooter(String text, boolean isHeader, String tagName) {

            }

        },
                ImporterUtils.DATA_FORMATTER_INTERNAL,
                false
        );
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
        table.removeInitialRows(rowInitial);
        return table;
    }

    private String getValueOrEmpty(List<String> fields, int index) {
        if (fields.isEmpty() || fields.size() <= index) {
            return "";
        }
        return fields.get(index);
    }

    public List<List<String>> getLines() throws SAXException, OpenXML4JException, ParserConfigurationException, IOException {
        return getXlsxLines(sheetNumber);
    }

    // Reavaliar daqui para baixo

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
        sheetNumber = getXSSFWorkbook().getFirstVisibleTab();
    }

    @Override
    public int getNumberOfSheets() {
        return getXSSFWorkbook().getNumberOfSheets();
    }

    @Override
    public List<T> parse() throws Exception {
        List<T> resultList = parseXlsx();
        if (ignoreBlankLinesAtEnd) {
            ImporterUtils.removeBlankLinesOfEnd(resultList, clazz);
        }
        return resultList;
    }

    private List<T> parseXlsx() throws SAXException, OpenXML4JException, ParserConfigurationException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Workbook xssfWorkbook = getXSSFWorkbook();
        Sheet sheet = xssfWorkbook.getSheetAt(sheetNumber);
        final FormulaEvaluator evaluator = xssfWorkbook.getCreationHelper().createFormulaEvaluator();

        Map<Method, TableCellMapping> cellMappingByMethod = ImporterUtils.getMappedMethods(clazz, group);
        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        List<T> list = new ArrayList<>();
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

    private XSSFWorkbook getXSSFWorkbook() {
        if (xssfWorkbook == null) {
            try {
                xssfWorkbook = new XSSFWorkbook(inputStream);
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
        return xssfWorkbook;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
*/

}
