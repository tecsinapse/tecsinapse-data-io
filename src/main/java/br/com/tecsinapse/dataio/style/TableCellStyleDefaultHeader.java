/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

final class TableCellStyleDefaultHeader extends TableCellStyleProtected {

    TableCellStyleDefaultHeader(boolean bold) {
        super(Colors.GREY_25_PERCENT, bold ? "header-bold" : "header", true, CellHAlign.CENTER, CellVAlign.CENTER, bold);
    }
}
