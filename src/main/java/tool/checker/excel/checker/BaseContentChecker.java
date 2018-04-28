package tool.checker.excel.checker;

import tool.checker.excel.BaseExcel;

public abstract class BaseContentChecker implements ContentChecker {
	
	protected BaseExcel excel;
	
	protected int row;

	@Override
	public void excelBegin(BaseExcel excel) {
		this.excel = excel;
		excelBegin();
	}

	@Override
	public void rowBegin(int row) {
		this.row = row;
		rowBegin();
	}

	@Override
	public void excelFinsih() {
		excelFinsihCall();
		excel = null;
	}
	
	protected abstract void excelBegin();
	
	protected abstract void rowBegin();
	
	protected abstract void excelFinsihCall();

}
