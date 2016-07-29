/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.exporter.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class CellStyleBorder implements Cloneable {

    public static CellStyleBorder DEFAULT = new CellStyleBorder(true, true, true, true);

    private HSSFColor borderColor;
    private boolean left;
    private boolean right;
    private boolean top;
    private boolean bottom;
    private short size = 1;

    public CellStyleBorder(boolean left, boolean right, boolean top, boolean bottom) {
        this(new HSSFColor.BLACK(), left, right, top, bottom);
    }

    public CellStyleBorder(HSSFColor borderColor, boolean left, boolean right, boolean top, boolean bottom) {
        this.borderColor = borderColor;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public HSSFColor getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(HSSFColor borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public CellStyle toCellStyle(CellStyle cellStyle) {
        if (!left && !right && !bottom && !top) {
            return cellStyle;
        }
        if (left) {
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setLeftBorderColor(borderColor.getIndex());
        }
        if (right) {
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setRightBorderColor(borderColor.getIndex());
        }
        if (bottom) {
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(borderColor.getIndex());
        }
        if (top) {
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(borderColor.getIndex());
        }
        return cellStyle;
    }

    public String toCss() {
        String format = "border%s:solid %s %dpx;";
        if (left && right && bottom && top) {
            return String.format(format, "", StyleColorUtil.toHexColor(borderColor), size);
        }
        if (!left && !right && !bottom && !top) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (left) {
            builder.append(String.format(format, "left", StyleColorUtil.toHexColor(borderColor), size));
        }
        if (right) {
            builder.append(String.format(format, "right", StyleColorUtil.toHexColor(borderColor), size));
        }
        if (bottom) {
            builder.append(String.format(format, "bottom", StyleColorUtil.toHexColor(borderColor), size));
        }
        if (top) {
            builder.append(String.format(format, "top", StyleColorUtil.toHexColor(borderColor), size));
        }
        return builder.toString();
    }

    public CellStyleBorder clone() {
        return new CellStyleBorder(getBorderColor(), isLeft(), isRight(), isTop(), isBottom());
    }

}
