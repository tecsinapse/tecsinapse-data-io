/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio;

import java.io.File;

public class ResourceUtils {

    public static File getFileResource(String name) {
        File resource = new File(ResourceUtils.class.getResource(name).getFile());
        if (!resource.exists()) {
            return null;
        }
        return resource;
    }

    public static File newFileTargetResource(String name) {
        File targetResourceDir = new File(ResourceUtils.class.getResource("/").getFile());
        return new File(targetResourceDir, name);
    }
}
