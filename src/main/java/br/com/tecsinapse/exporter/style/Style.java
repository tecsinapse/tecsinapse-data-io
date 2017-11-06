/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.style;

public final class Style {

    private Style() {}

    public static final String BACKGROUND_COLOR_GRAY = "background-color: rgb(221, 221, 221);";
    public static final String FONT_SIZE_1_2_EM = "font-size: 1.2em;";
    public static final String FONT_SIZE_1_4_EM = "font-size: 1.4em;";
    public static final String FONT_SIZE_1_6_EM = "font-size: 1.6em;";
    public static final String FONT_SIZE_1_8_EM = "font-size: 1.8em;";
    public static final String FONT_WEIGHT_BOLD = "font-weight: bold;";
    public static final String TEXT_ALIGN_LEFT = "text-align: left;";
    public static final String VERTICAL_ALIGN_MIDDLE = "vertical-align: middle;";

    public static final CellStyleBorder CELL_STYLE_BORDER_DEFAULT = new CellStyleBorderDefault();

    public static final TableCellStyle TABLE_CELL_STYLE_HEADER = new TableCellStyleDefaultHeaderFooter();
    public static final TableCellStyle TABLE_CELL_STYLE_BODY = new TableCellStyleDefaultBody(false);
    public static final TableCellStyle TABLE_CELL_STYLE_BODY_CENTER = new TableCellStyleDefaultBody(true);
    public static final TableCellStyle TABLE_CELL_STYLE_FOOTER = new TableCellStyleDefaultHeaderFooter();

}
