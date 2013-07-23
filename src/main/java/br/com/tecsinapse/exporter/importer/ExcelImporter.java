package br.com.tecsinapse.exporter.importer;

import br.com.tecsinapse.exporter.annotation.TableCellMapping;
import br.com.tecsinapse.exporter.converter.TableCellConverter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.reflections.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
public class ExcelImporter<T> {

    private final Class<T> clazz;
    private File excel;
    private InputStream excelInputStream;

    public ExcelImporter(Class<T> clazz, File file) throws IOException {
        this(clazz);
        this.excel = file;
    }
    public ExcelImporter(Class<T> clazz, InputStream inputStream) throws IOException {
        this(clazz);
        this.excelInputStream = inputStream;
    }

    private ExcelImporter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public <X> List<T> parse() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, InvalidFormatException {
        List<T> list = new ArrayList<>();
        Set<Method> methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(TableCellMapping.class));
        Workbook wb =  getWorkbook();
        Sheet sheet = wb.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();

        boolean header = true;
        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(header) {
                header = false;
                continue;
            }
            List<String> fields = getFields(row);
            T instance = clazz.newInstance();

            for (Method method : methods) {
                TableCellMapping tcm = method.getAnnotation(TableCellMapping.class);
                String value = getValueOrEmpty(fields, tcm.columnIndex());
                TableCellConverter converter = tcm.converter().newInstance();
                Object obj = converter.apply(value);
                method.invoke(instance, obj);
            }
            list.add(instance);
        }
        return list;
    }

    private List<String> getFields(Row row) {
        List<String> values= new ArrayList<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        while(cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            switch(cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    values.add(Boolean.valueOf(cell.getBooleanCellValue()).toString());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    values.add(Double.valueOf(cell.getNumericCellValue()).toString());
                    break;
                case Cell.CELL_TYPE_STRING:
                    values.add(cell.getStringCellValue());
                    break;
            }
        }
        return values;
    }

    private String getValueOrEmpty(List<String> fields, int index) {
        if (fields.isEmpty() || fields.size() <= index) {
            return "";
        }
        return fields.get(index);
    }

    public Workbook getWorkbook() throws IOException, InvalidFormatException {
        if(excel != null) {
            return WorkbookFactory.create(excel);
        }
        return WorkbookFactory.create(excelInputStream);
    }
}
