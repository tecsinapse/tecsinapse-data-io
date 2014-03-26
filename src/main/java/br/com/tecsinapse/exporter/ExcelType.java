package br.com.tecsinapse.exporter;

public enum ExcelType {
	XLS, XLSX;

    public static ExcelType getExcelType(String filename) {
        if (filename.toLowerCase().endsWith("xlsx")) {
            return ExcelType.XLSX;
        }
        return ExcelType.XLS;
    }
}
