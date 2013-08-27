package br.com.tecsinapse.exporter;

public enum FileType {
	XLS, XLSX, CSV;

    public static FileType getFileType(String filename) {
        if (filename.endsWith("xlsx")) {
            return FileType.XLSX;
        } else if(filename.endsWith("xls")) {
            return FileType.XLS;
        }
        return CSV;
    }

    public ExcelType getExcelType() {
        if(this == XLS) {
            return ExcelType.XLS;
        }
        if(this == XLSX) {
            return ExcelType.XLSX;
        }
        return null;
    }
}
