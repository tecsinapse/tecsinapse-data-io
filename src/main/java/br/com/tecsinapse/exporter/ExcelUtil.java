package br.com.tecsinapse.exporter;

import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    private static HttpServletResponse getResponseForExcel(String filename,
                                                           FacesContext context) {
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext()
                .getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-disposition", "attachment;filename="
                + filename + ".xls");
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

    public static void export(String name, Table t) throws IOException {
        Workbook wb = t.toWorkBook();

        String filename = name + "_";
        filename += new DateTime(new Date()).toString("dd-MM-yyyy_HH-mm");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = getResponseForExcel(
                filename, context);
        wb.write(response.getOutputStream());
        context.responseComplete();

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
    	try (FileOutputStream fos = new FileOutputStream(f)) {
    		CSVUtil.write(t.toStringMatrix(), fos, charsetName);
		}
    	return f;
    }
    
    public static File getXlsFile(Table t, String file) throws IOException {
    	
    	File f = new File(file);
    	try (FileOutputStream fos = new FileOutputStream(f)) {
    		t.toWorkBook().write(fos);
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
