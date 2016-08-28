/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter;

import br.com.tecsinapse.exporter.style.Style;
import br.com.tecsinapse.exporter.style.TableCellStyle;

/**
 * Does some thing in old style. It will be removed in version 2.0.0
 *
 * @deprecated use {@link br.com.tecsinapse.exporter.style.TableCellStyle}
 */
@Deprecated
public enum TableCellType {

    BODY("body", Style.VERTICAL_ALIGN_MIDDLE, TableCellStyle.BODY),
    FOOTER("footer", Style.BACKGROUND_COLOR_GRAY + Style.FONT_WEIGHT_BOLD + Style.VERTICAL_ALIGN_MIDDLE + Style.FONT_SIZE_1_2_EM, TableCellStyle.FOOTER),
    HEADER("header", Style.BACKGROUND_COLOR_GRAY + Style.FONT_WEIGHT_BOLD + Style.VERTICAL_ALIGN_MIDDLE + Style.FONT_SIZE_1_2_EM, TableCellStyle.HEADER);

    private final String name;
    private final String defaultStyle;
    private final TableCellStyle tableCellStyle;

    TableCellType(String name, String defaultStyle, TableCellStyle tableCellStyle) {
        this.name = name;
        this.defaultStyle = defaultStyle;
        this.tableCellStyle = tableCellStyle;
    }

    public String getName() {
        return name;
    }

    public String getDefaultStyle() {
        return defaultStyle;
    }

    public TableCellStyle getTableCellStyle() {
        return tableCellStyle;
    }
}
