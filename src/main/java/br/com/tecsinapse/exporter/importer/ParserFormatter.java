/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

public class ParserFormatter {

    public static final ParserFormatter PT_BR = new ParserFormatter("dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy", "HH:mm");
    public static final ParserFormatter DEFAULT = new ParserFormatter("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm");

    private final String localDateTimeFormat;
    private final String localDateFormat;
    private final String localTimeFormat;

    public ParserFormatter(String localDateTimeFormat, String localDateFormat, String localTimeFormat) {
        this.localDateTimeFormat = localDateTimeFormat;
        this.localDateFormat = localDateFormat;
        this.localTimeFormat = localTimeFormat;
    }

    public String getLocalDateTimeFormat() {
        return localDateTimeFormat;
    }

    public String getLocalDateFormat() {
        return localDateFormat;
    }

    public String getLocalTimeFormat() {
        return localTimeFormat;
    }
}
