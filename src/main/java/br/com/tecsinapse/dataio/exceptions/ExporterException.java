/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.exceptions;

public class ExporterException extends RuntimeException {

    private static final long serialVersionUID = 8323831175219556054L;

    public ExporterException(String message) {
        super(message);
    }
}
