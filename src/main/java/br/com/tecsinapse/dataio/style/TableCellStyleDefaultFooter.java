/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

final class TableCellStyleDefaultFooter extends TableCellStyleProtected {

    TableCellStyleDefaultFooter(boolean bold) {
        super(Colors.GREY_25_PERCENT, bold ? "footer-bold" : "footer", true, CellHAlign.CENTER, CellVAlign.CENTER, bold);
    }
}
