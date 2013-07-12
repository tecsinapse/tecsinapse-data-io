package br.com.tecsinapse.exporter;

public enum TableCellType {
	BODY("body"), FOOTER("footer"), HEADER("header");

	private String name;

	TableCellType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
