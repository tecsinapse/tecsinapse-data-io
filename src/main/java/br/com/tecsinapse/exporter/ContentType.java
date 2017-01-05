package br.com.tecsinapse.exporter;

public enum ContentType {
	TEXT("text"),
    HTML("html");

    private final String name;

	ContentType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isText() {
		return this == TEXT;
	}

	public boolean isHtml() {
		return this == HTML;
	}
}
