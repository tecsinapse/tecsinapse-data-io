/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package br.com.tecsinapse.dataio.style;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.WHITE;

import org.apache.poi.hssf.util.HSSFColor;

public enum CssStyle {
    BOLD("font-weight:bold;"),
    ITALIC("font-style:italic;"),
    UNDERLINE("text-decoration:underline;"),
    STRIKEOUT("text-decoration:line-through;");

    private final String css;

    CssStyle(String css) {
        this.css = css;
    }

    public String getCss() {
        return css;
    }

    public static String toBackgroundColor(HSSFColor hssfColor) {
        return toBackgroundColor(hssfColor, false);
    }

    public static String toBackgroundColor(HSSFColor hssfColor, boolean whiteAsTransparent) {
        if (hssfColor == null) {
            return "";
        }

        if (whiteAsTransparent && WHITE.getHexString().equals(hssfColor.getHexString())) {
            return "";
        }

        return String.format("background-color:%s;", StyleColorUtil.toHexColor(hssfColor));
    }

    public static String toTextColor(HSSFColor hssfColor) {
        if (hssfColor == null) {
            return "";
        }
        return String.format("color:%s;", StyleColorUtil.toHexColor(hssfColor));
    }

    public static String toFontSize(int fontSize) {
        return String.format("font-size:%d;", fontSize);
    }

}
