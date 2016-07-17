/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter;

import br.com.tecsinapse.exporter.type.TableCellType;

public final class EmptyTableCell extends TableCell {

    public static final EmptyTableCell EMPTY_CELL = new EmptyTableCell();

    private EmptyTableCell() {
    }

    @Override
    public void setColspan(Integer colspan) {
        throw new UnsupportedOperationException(
                "EmptyTableCell: não é possível alterar o conteúdo");
    }

    @Override
    public void setContent(String content) {
        throw new UnsupportedOperationException(
                "EmptyTableCell: não é possível alterar o conteúdo");
    }

    @Override
    public void setRowspan(Integer rowspan) {
        throw new UnsupportedOperationException(
                "EmptyTableCell: não é possível alterar o conteúdo");
    }

    @Override
    public void setTableCellType(TableCellType tableCellType) {
        throw new UnsupportedOperationException(
                "EmptyTableCell: não é possível alterar o conteúdo");
    }
}
