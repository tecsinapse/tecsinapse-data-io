package br.com.tecsinapse.exporter;

import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        List<List<String>> csv = t.toStringMatrix();

        String filename = name + "_";
        filename += new DateTime(new Date()).toString("dd-MM-yyyy_HH-mm");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = getResponseForCsv(filename, context);
        CSVUtil.write(csv, response.getOutputStream(), chartsetName);
        context.responseComplete();
    }
}
