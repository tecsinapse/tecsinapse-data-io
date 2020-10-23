/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.util;

import com.google.common.base.Strings;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SpreadsheetUtil {

    public static int getColumnIndexByColumnName(String columnName) {
        columnName = getColumnFromCellName(columnName.toUpperCase());
        int value = 0;
        for (int i = 0; i < columnName.length(); i++) {
            int delta = (columnName.charAt(i)) - 64;
            value = value * 26 + delta;
        }
        return value - 1;
    }

    private static String getColumnFromCellName(String cellReference) {
        if (Strings.isNullOrEmpty(cellReference)) {
            return "";
        }

        return cellReference.split("[0-9]*$")[0];
    }

    public static String getColumnNameByColumnIndex(int columnIndex) {
        StringBuilder s = new StringBuilder();
        do {
            s.insert(0, (char) ('A' + (columnIndex % 26)));
            columnIndex /= 26;
        } while (columnIndex-- > 0);
        return s.toString();
    }

}
