package br.com.tecsinapse.exporter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
}
