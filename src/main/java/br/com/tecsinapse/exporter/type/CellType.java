/*
 * TecSinapse Exporter
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
    NUMERIC_TYPE(true),
    DATETIME_TYPE(true),
    DATE_TYPE(true),
    TIME_TYPE(true),
    CURRENCY_TYPE(true),
    @Deprecated
    BRL_TYPE(true);

    private final boolean allowFormat;

    CellType(boolean allowFormat) {
        this.allowFormat = allowFormat;
    }

    public static CellType byObject(Object o) {
        if (o instanceof Number) {
            return NUMERIC_TYPE;
        }
        if (o instanceof Date) {
            final DateType dateType = ExporterDateUtils.getDateType((Date) o);
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
}
