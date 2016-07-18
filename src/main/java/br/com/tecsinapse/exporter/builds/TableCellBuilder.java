/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.builds;

import br.com.tecsinapse.exporter.TableCell;
import br.com.tecsinapse.exporter.type.TableCellType;

public class TableCellBuilder {

    private final TableCell toBuild;

    public static TableCellBuilder withTableCellBuilder(TableCell tableCellBuilder) {
        return new TableCellBuilder(tableCellBuilder);
    }

    public static TableCellBuilder newTableCellBuilder() {
        return withTableCellBuilder(new TableCell());
    }

    private TableCellBuilder(TableCell tableCell) {
        this.toBuild = tableCell;
    }

    public TableCellBuilder content(Object content) {
        toBuild.setContent(content);
        return this;
    }

    public TableCellBuilder tableCellType(TableCellType tableCellType) {
        toBuild.setTableCellType(tableCellType);
        return this;
    }

    public TableCellBuilder style(String style) {
        toBuild.setStyle(style);
        return this;
    }

    public TableCellBuilder colspan(int colspan) {
        toBuild.setColspan(colspan);
        return this;
    }

    public TableCellBuilder rowspan(int rowspan) {
        toBuild.setRowspan(rowspan);
        return this;
    }

    public TableCellBuilder bold(boolean bold) {
        toBuild.setBold(bold);
        return this;
    }

    public TableCell build() {
        return toBuild;
    }
}
