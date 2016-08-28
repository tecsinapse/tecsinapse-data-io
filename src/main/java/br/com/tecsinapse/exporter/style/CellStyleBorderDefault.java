/*
 * Tecsinapse Data Importer and Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.apache.poi.hssf.util.HSSFColor;

final class CellStyleBorderDefault extends CellStyleBorder {

    public CellStyleBorderDefault() {
        super(true, true, true, true);
    }

    private void throwUnsupportedOperationException() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("CellStyleBorderDefault: Unsupported operation for default objects.");
    }

    @Override
    public void setBorderColor(HSSFColor borderColor) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setBottom(boolean bottom) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setLeft(boolean left) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setRight(boolean right) {
        throwUnsupportedOperationException();
    }

    @Override
    public void setTop(boolean top) {
        throwUnsupportedOperationException();
    }
}
