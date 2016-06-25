/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.importer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.joda.time.LocalDateTime;

public enum ImporterXLSXType {

	DEFAULT(){
        @Override
        public DataFormatter getFormatter(ExcelParser<?> parser) {
            return new DefaultDataFormat(parser);
        }
    }, UNIQUE_DATA_VALUE(){
        @Override
        public DataFormatter getFormatter(ExcelParser<?> parser) {
            return new UniqueDataFormat();
        }
    };

    abstract DataFormatter getFormatter(ExcelParser<?> parser);

	private static class UniqueDataFormat extends DataFormatter {

		@Override
		public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing) {
			// Is it a date? then always format like LocalDateTime default Formatter
			if(DateUtil.isADateFormat(formatIndex,formatString) && DateUtil.isValidExcelDate(value)) {
                Date d = DateUtil.getJavaDate(value, use1904Windowing);
                return LocalDateTime.fromDateFields(d).toString();
            } else {
            	// else Number
            	// If is General formating then take the value from super
            	if ("General".equalsIgnoreCase(formatString)) {
            		return super.formatRawCellContents(value, formatIndex, formatString, use1904Windowing);
            	}
            	// then format as toString output for BigDecimal. See the BigDecimal.toString() document to know why.
            	return BigDecimal.valueOf(value).toString();
            }
		}
	}

    private static class DefaultDataFormat extends DataFormatter {
        private final ExcelParser<?> parser;

        private DefaultDataFormat(ExcelParser<?> parser) {
            super(Locale.ENGLISH);
            this.parser = parser;
        }

        @Override
        public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing) {
            // Is it a date? then always format like LocalDateTime default Formatter
            if (parser != null && DateUtil.isADateFormat(formatIndex, formatString) && DateUtil.isValidExcelDate(value)) {
                Date d = DateUtil.getJavaDate(value, use1904Windowing);

                return LocalDateTime.fromDateFields(d).toString(parser.getDateStringPattern());
            } else {
                return super.formatRawCellContents(value, formatIndex, formatString, use1904Windowing);
            }
        }
    }
}
