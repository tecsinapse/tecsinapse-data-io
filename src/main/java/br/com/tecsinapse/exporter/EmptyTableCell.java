/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import br.com.tecsinapse.exporter.style.TableCellStyle;

public final class EmptyTableCell extends TableCell {

    public static final EmptyTableCell EMPTY_CELL = new EmptyTableCell();

    private EmptyTableCell() {
    }

    private void throwUnsupportedOperationException() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("EmptyTableCell: Unsupported operation for default objects.");
    }

    @Override
    public void setColspan(Integer colspan) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setContent(Object content) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setRowspan(Integer rowspan) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setTableCellStyle(TableCellStyle tableCellStyle) {
        throwUnsupportedOperationException();
    }

}
