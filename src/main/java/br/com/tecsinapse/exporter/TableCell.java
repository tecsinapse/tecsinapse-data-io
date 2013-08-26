package br.com.tecsinapse.exporter;

public class TableCell {
	private String content = " ";
	private Integer colspan = 1;
	private Integer rowspan = 1;
	private TableCellType tableCellType = TableCellType.BODY;

	public TableCell() {
		super();
	}

	public TableCell(String content) {
		super();
		this.content = 
				(content == null || content.isEmpty()) ? " " : content;
	}

	public TableCell(String content, TableCellType tableCellType) {
		super();
		this.content = content;
		this.tableCellType = tableCellType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getColspan() {
		return colspan;
	}

	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}

	public Integer getRowspan() {
		return rowspan;
	}

	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}

	public TableCellType getTableCellType() {
		return tableCellType;
	}

	public void setTableCellType(TableCellType tableCellType) {
		this.tableCellType = tableCellType;
	}

}
