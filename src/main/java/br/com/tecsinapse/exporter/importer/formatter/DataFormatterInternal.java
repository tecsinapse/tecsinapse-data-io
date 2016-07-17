/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer.formatter;

import java.util.Date;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.joda.time.LocalDateTime;

public final class DataFormatterInternal extends DataFormatter {

    private static final String ISO_DATE_TIME_FORMAT = "yyyyMMdd'T'HHmmss.SSS";

    @Override
    public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing) {
        if (DateUtil.isADateFormat(formatIndex, formatString) && DateUtil.isValidExcelDate(value)) {
            Date d = DateUtil.getJavaDate(value, use1904Windowing);
            return LocalDateTime.fromDateFields(d).toString(ISO_DATE_TIME_FORMAT);
        } else {
            return super.formatRawCellContents(value, formatIndex, formatString, use1904Windowing);
        }
    }
}
