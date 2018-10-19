/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

final class TableCellStyleDefaultBody extends TableCellStyleProtected {
    TableCellStyleDefaultBody(boolean center, boolean bold) {
        super(Colors.WHITE, bold ? "body-bold" : "body", true, center ? CellHAlign.CENTER : CellHAlign.LEFT, CellVAlign.CENTER, bold);
    }
}
