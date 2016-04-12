package br.com.tecsinapse.exporter.txt;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDateTime;

public class TxtUtil {

	public static void exportFile(FileTxt file, String fileName, String chartsetName) throws IOException {
		String filename = String.format("%s_%s", fileName, LocalDateTime.now().toString("dd-MM-yyyy_HH-mm"));
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = getResponseForTxt(filename, context);
		export(file, chartsetName, response.getOutputStream());
		context.responseComplete();
	}

	private static void export(FileTxt file, String chartsetName, OutputStream out) throws IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(out, chartsetName)) {
			for (List<FieldTxt> lines : file.getFields()) {
				StringBuilder line = new StringBuilder();
				for (Iterator<FieldTxt> it = lines.iterator(); it.hasNext(); ) {
					FieldTxt field = it.next();
					line.append(field.getValue());
					if (it.hasNext() || file.isEndsWithSeparator()) {
						line.append(field.getSeparator());
					}
				}
				line.append("\r\n");
				writer.write(line.toString());
			}
			writer.flush();
		}
	}

	private static HttpServletResponse getResponseForTxt(String filename, FacesContext context) {
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.setContentType("text/plain");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setHeader("Content-disposition", String.format("attachment;filename=%s.txt", filename));
		return response;
	}

}
