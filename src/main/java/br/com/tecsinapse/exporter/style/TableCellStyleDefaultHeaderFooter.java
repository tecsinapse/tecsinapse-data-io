/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

final class TableCellStyleDefaultHeaderFooter extends TableCellStyleProtected {

    TableCellStyleDefaultHeaderFooter() {
        super(Colors.GREY_25_PERCENT, "header", true, CellHAlign.CENTER, CellVAlign.CENTER);
    }
}
