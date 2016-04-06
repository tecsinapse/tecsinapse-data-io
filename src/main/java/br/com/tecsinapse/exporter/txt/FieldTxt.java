package br.com.tecsinapse.exporter.txt;

/**
 * Representa cada coluna de uma linha no arquivo .txt<br><br/>
 * Exemplo de criação de um FieldTxt:<br/><br/>
 * <code>
 *     FieldTxt.newBuilder()<br/>
 *     		.withContent("12345")<br/>
 *     		.withAlign(FieldTxtAlign.RIGHT)<br/>
 *     		.withFixedSize(20)<br/>
 *     		.withFiller("0")<br/>
 *     		.withSeparator(SeparatorTxt.TAB)<br/>
 *     		.build();<br/><br/>
 * </code>
 * Valores padrões:<br/>
 * <li>separator: Separator.NONE</li><br/>
 * <li>fixedSize: 0</li><br/>
 * <li>filler: ' '</li><br/>
 * <li>align: FieldTxtAlign.LEFT</li><br/>
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
