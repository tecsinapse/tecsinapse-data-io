/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.io.ByteStreams;

import br.com.tecsinapse.exporter.servlet.ExportServletUtil;

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

    /**
     * This method is moved. It will be removed in version 2.0.0
     *
     * @param fileName fileName
     * @param fileExtension fileExtension
     * @param baos baos
     * @deprecated use methods from {@link br.com.tecsinapse.exporter.servlet.ExportServletUtil}
     * @throws IOException IOException
     */
    @Deprecated
    public static void exportZip(String fileName, String fileExtension, ByteArrayOutputStream baos) throws IOException {
        ExportServletUtil.facesDownloadZip(baos, fileName + "." + fileExtension);
    }

}
