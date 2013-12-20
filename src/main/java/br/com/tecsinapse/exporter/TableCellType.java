package br.com.tecsinapse.exporter;

public enum TableCellType {
	BODY("body", ""), FOOTER("footer", "background-color: rgb(221, 221, 221);"), HEADER("header", "background-color: rgb(221, 221, 221);");

    private final String name;
    private final String defaultStyle;

	TableCellType(String name, String defaultStyle) {
		this.name = name;
        this.defaultStyle = defaultStyle;
	}

	public String getName() {
		return name;
	}

    public String getDefaultStyle() {
        return defaultStyle;
    }
}
