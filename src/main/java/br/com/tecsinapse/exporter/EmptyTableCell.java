package br.com.tecsinapse.exporter;

final class EmptyTableCell extends TableCell {
	static final EmptyTableCell EMPTY_CELL = new EmptyTableCell();

	private EmptyTableCell() {
	}
	
	@Override
	public void setColspan(Integer colspan) {
		throw new UnsupportedOperationException(
				"EmptyTableCell: não é possível alterar o conteúdo");
	}

	@Override
	public void setContent(String content) {
		throw new UnsupportedOperationException(
				"EmptyTableCell: não é possível alterar o conteúdo");
	}

	@Override
	public void setRowspan(Integer rowspan) {
		throw new UnsupportedOperationException(
				"EmptyTableCell: não é possível alterar o conteúdo");
	}

	@Override
	public void setTableCellType(TableCellType tableCellType) {
		throw new UnsupportedOperationException(
				"EmptyTableCell: não é possível alterar o conteúdo");
	}
}
