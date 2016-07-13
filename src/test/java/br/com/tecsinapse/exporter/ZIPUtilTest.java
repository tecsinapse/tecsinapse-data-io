/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import java.io.File;

import org.testng.annotations.Test;

public class ZIPUtilTest {

    @Test
    public void testZip() throws Exception {
        File[] files = new File[]{
                ResourceUtils.getFileResource("/files/excel.xls"),
                ResourceUtils.getFileResource("/files/excel.xlsx")
        };

        File outZip1 = ResourceUtils.newFileTargetResource("zipOut-1.zip");
        ZIPUtil.zip(outZip1, files);
    }

}