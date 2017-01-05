package br.com.tecsinapse.exporter;

public enum TableCellType {
	BODY("body", Style.VERTICAL_ALIGN_MIDDLE),
    FOOTER("footer", Style.BACKGROUND_COLOR_GRAY + Style.FONT_WEIGHT_BOLD + Style.VERTICAL_ALIGN_MIDDLE + Style.FONT_SIZE_1_2_EM),
    HEADER("header", Style.BACKGROUND_COLOR_GRAY + Style.FONT_WEIGHT_BOLD + Style.VERTICAL_ALIGN_MIDDLE + Style.FONT_SIZE_1_2_EM);

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

    public boolean isHeader() {
		return this == HEADER;
	}
}
