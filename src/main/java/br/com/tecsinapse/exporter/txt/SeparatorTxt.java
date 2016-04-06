package br.com.tecsinapse.exporter.txt;

/**
 * Representa os tipos de separadores possíveis para utilizar entre as colunas do arquivo .txt gerado.<br/>
 * <b>Separadores:</b><br/>
 * <li>TAB: \t (Tabulação)<br/></li>
 * <li>NONE: '' (Vazio)<br/></li>
 * <li>SPACE: ' ' (Espaço em branco)<br/></li>
 * <li>SEMICOLON: ';' (Ponto e vírgula)<br/></li>
 * <li>COMMA: ',' (Vírgula)<br/></li>
 */
public enum SeparatorTxt {
	TAB("\t"),
	NONE(""),
	SPACE(" "),
	SEMICOLON(";"),
	COMMA(",");

	SeparatorTxt(String separator) {
		this.separator = separator;
	}

	private final String separator;

	public String getSeparator() {
		return separator;
	}
}
