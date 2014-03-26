package br.com.tecsinapse.exporter;

import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    private static HttpServletResponse getResponseForExcel(String filenameWithExtension,
                                                           FacesContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext()
                .getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename="
                + filenameWithExtension);
        return response;
    }

    private static HttpServletResponse getResponseForCsv(String filename,
                                                         FacesContext context) {
        HttpServletResponse response = (HttpServletResponse) context
                .getExternalContext().getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename="
                + filename + ".csv");
        return response;
    }

    private static void doExport(String name, String extension, Workbook wb) throws IOException {
        String filename = name + "_";
        filename += LocalDateTime.now().toString("dd-MM-yyyy_HH-mm");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = getResponseForExcel(filename + extension, context);
        wb.write(response.getOutputStream());
        context.responseComplete();
    }

    /**
     * @deprecated use {@link void exportXls(String name, Table t)} instead.
     */
    @Deprecated
    public static void export(String name, Table t) throws IOException {
        exportXls(name, t);
    }

    public static void exportXls(String name, Table t) throws IOException {
        Workbook wb = t.toHSSFWorkBook();
        doExport(name, ".xls", wb);
    }

    public static void exportXlsx(String name, Table t) throws IOException {
        Workbook wb = t.toXSSFWorkBook();
        doExport(name, ".xlsx", wb);
    }

    public static void exportCsv(String name, Table t, String chartsetName) throws IOException {
        String filename = name + "_";
        filename += new DateTime(new Date()).toString("dd-MM-yyyy_HH-mm");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = getResponseForCsv(filename, context);
        
		  exportCsv(t, chartsetName, response.getOutputStream());
		  
        context.responseComplete();
    }
	 
    public static void exportCsv(Table t, String chartsetName, OutputStream out) throws IOException {
        List<List<String>> csv = t.toStringMatrix();
        CSVUtil.write(csv, out, chartsetName);
    }
    
    public static File getCsvFile(Table t, String file, String charsetName) throws IOException {

    	File f = new File(file);
    	try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
    		CSVUtil.write(t.toStringMatrix(), fos, charsetName);
		}
    	return f;
    }
    
    public static File getXlsFile(Table t, String file) throws IOException {
    	
    	File f = new File(file);
    	try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f))) {
    		t.toHSSFWorkBook().write(fos);
		}
    	return f;
    }

    public static int getColumnIndexByColumnName(String columnName) {
        columnName = getColumnFromCellName(columnName.toUpperCase());
        int value = 0;
        for (int i = 0; i < columnName.length(); i++) {
            int delta = (columnName.charAt(i)) - 64;
            value = value * 26 + delta;
        }
        return value - 1;
    }

    private static String getColumnFromCellName(String cellReference) {
        if (Strings.isNullOrEmpty(cellReference)) {
            return "";
        }

        return cellReference.split("[0-9]*$")[0];
    }

    public static String getColumnNameByColumnIndex(int columnIndex) {
        String s = "";
        do {
            s = (char) ('A' + (columnIndex % 26)) + s;
            columnIndex /= 26;
        } while (columnIndex-- > 0);
        return s;
    }
}
