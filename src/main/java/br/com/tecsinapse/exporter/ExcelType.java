package br.com.tecsinapse.exporter;

public enum ExcelType {
	XLS("dd/MM/yyyy"), XLSX("MM/dd/yyyy"), XLSM("MM/dd/yyyy");

    private final String defaultDatePattern;

    private ExcelType(String defaultDatePattern) {
        this.defaultDatePattern = defaultDatePattern;
    }

    public String getDefaultDatePattern() {
        return defaultDatePattern;
    }

    public static ExcelType getExcelType(String filename) {
        if (filename.toLowerCase().endsWith("xlsx")) {
            return ExcelType.XLSX;
        }
        if (filename.toLowerCase().endsWith("xlsm")) {
            return ExcelType.XLSM;
        }
        return ExcelType.XLS;
    }
}
