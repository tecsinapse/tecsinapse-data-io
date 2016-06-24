/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.ByteStreams;

public final class ZIPUtil {

	private ZIPUtil() {
	}

	public static void zip(File newZip, File... filesToAdd) throws IOException {
		try (ZipOutputStream zos = new ZipOutputStream(
				new BufferedOutputStream(new FileOutputStream(newZip)))) {
			for (File file : filesToAdd) {
				addFile(zos, file);
			}
		}
	}

	public static void addFile(ZipOutputStream zos, File file) throws IOException {
		if (file.isDirectory()) {
			ZipEntry entry = new ZipEntry(file.getName());
			zos.putNextEntry(entry);
			for (File subFile : file.listFiles()) {
				addFile(zos, subFile);
			}
			zos.closeEntry();
		}

		try (FileInputStream fis = new FileInputStream(file)) {
			ZipEntry entry = new ZipEntry(file.getName());
			zos.putNextEntry(entry);

			ByteStreams.copy(fis, zos);

			zos.closeEntry();
		}
	}

	public static void exportZip(String fileName, String fileExtension, ByteArrayOutputStream baos) throws IOException {
		final FacesContext context = FacesContext.getCurrentInstance();
		final HttpServletResponse response = getResponseForZip(fileName, context);

		try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
			zipOutputStream.putNextEntry(new ZipEntry(fileName + "." + fileExtension));
			zipOutputStream.write(baos.toByteArray());
			zipOutputStream.closeEntry();
		}

		context.responseComplete();
	}

	public static HttpServletResponse getResponseForZip(String fileName, FacesContext context) {
		final HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
		response.setContentType("application/zip");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".zip");
		return response;
	}

}
