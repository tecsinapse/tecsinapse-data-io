/*
 * TecSinapse Exporter
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.exporter.txt;

/**
 * Representa cada coluna de uma linha no arquivo {@code .txt}
 * <br><br>
 * Exemplo de criação de um FieldTxt:
 * <p>
 * <pre>
 * {@code
 *     FieldTxt.newBuilder()
 *          .withContent("12345")
 *          .withAlign(FieldTxtAlign.RIGHT)
 *          .withFixedSize(20)
 *          .withFiller("0")
 *          .withSeparator(SeparatorTxt.TAB)
 *          .build();
 * }
 * </pre>
 * Valores padrões:
 * <p>
 * <ul>
 * <li>separator: Separator.NONE</li>
 * <li>fixedSize: 0</li>
 * <li>filler: ' '</li>
 * <li>align: FieldTxtAlign.LEFT</li>
 * </ul>
 */
public class FieldTxt {

    private String content;
    private FieldTxtAlign align;
    private SeparatorTxt separator;
    private int fixedSize;
    private String filler;

    private FieldTxt(Builder builder) {
        this.content = builder.content;
        this.align = builder.align;
        this.separator = builder.separator;
        this.fixedSize = builder.fixedSize;
        this.filler = builder.filler;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public FieldTxtAlign getAlign() {
        return align;
    }

    public String getContent() {
        return content != null ? content : "";
    }

    public String getSeparator() {
        return separator.getSeparator();
    }

    public int getFixedSize() {
        return fixedSize;
    }

    public String getFiller() {
        return filler;
    }

    public String getValue() {
        return align.getValueAligned(this);
    }

    public static final class Builder {

        private String content;
        private FieldTxtAlign align = FieldTxtAlign.LEFT;
        private SeparatorTxt separator = SeparatorTxt.NONE;
        private int fixedSize = 0;
        private String filler = " ";

        private Builder() {
        }

        public FieldTxt build() {
            return new FieldTxt(this);
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withAlign(FieldTxtAlign align) {
            this.align = align;
            return this;
        }

        public Builder withSeparator(SeparatorTxt separator) {
            this.separator = separator;
            return this;
        }

        public Builder withFixedSize(int fixedSize) {
            this.fixedSize = fixedSize;
            return this;
        }

        public Builder withFiller(String filler) {
            this.filler = filler;
            return this;
        }
    }
}
