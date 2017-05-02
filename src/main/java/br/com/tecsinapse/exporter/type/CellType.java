/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.type;

import java.util.Date;

import br.com.tecsinapse.exporter.util.ExporterDateUtils;
import br.com.tecsinapse.exporter.util.ExporterDateUtils.DateType;

public enum CellType {

    STRING_TYPE(false),
    HTML_TYPE(false, "html"),
    NUMERIC_TYPE(true),
    DATETIME_TYPE(true),
    DATE_TYPE(true),
    TIME_TYPE(true),
    CURRENCY_TYPE(true);

    private final boolean allowFormat;
    private final String contenType;

    CellType(boolean allowFormat) {
        this(allowFormat, "text");
    }

    CellType(boolean allowFormat, String contenType) {
        this.allowFormat = allowFormat;
        this.contenType = contenType;
    }

    public static CellType byObject(Object o) {
        if (o instanceof Number) {
            return NUMERIC_TYPE;
        }
        if (o instanceof Date) {
            final DateType dateType = ExporterDateUtils.getDateType((Date) o);
            if (dateType == DateType.NO_DATE) {
                return STRING_TYPE;
            }
            if (dateType == DateType.DATE) {
                return DATE_TYPE;
            }
            if (dateType == DateType.TIME) {
                return TIME_TYPE;
            }

            return DATETIME_TYPE;
        }
        return STRING_TYPE;
    }

    public boolean isAllowFormat() {
        return allowFormat;
    }

    public String getContenType() {
        return contenType;
    }
}
