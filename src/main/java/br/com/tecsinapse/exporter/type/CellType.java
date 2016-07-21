/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.type;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public enum CellType {

    STRING_TYPE(false),
    NUMERIC_TYPE(true),
    LOCAL_DATE_TIME_TYPE(true),
    LOCAL_DATE_TYPE(true),
    LOCAL_TIME_TYPE(true),
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
        if (o instanceof LocalDateTime) {
            return LOCAL_DATE_TIME_TYPE;
        }
        if (o instanceof LocalDate) {
            return LOCAL_DATE_TYPE;
        }
        if (o instanceof LocalTime) {
            return LOCAL_TIME_TYPE;
        }
        return STRING_TYPE;
    }

    public boolean isAllowFormat() {
        return allowFormat;
    }
}
