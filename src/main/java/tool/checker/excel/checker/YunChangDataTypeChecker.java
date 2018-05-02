package tool.checker.excel.checker;

import com.google.common.base.Strings;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;

public final class YunChangDataTypeChecker extends BaseContentChecker {

	@Override
	public boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher) {
		if (!Strings.isNullOrEmpty(content)) {
			String excelName = excel.getExcelName();
			switch (item.getType().toLowerCase()) {
			case "int" : 
				readInt(row, content, item.getName(), errorCatcher, excelName);
				break;
			case "array_int" : 
				readArrayInt(row, content, item.getName(), errorCatcher, excelName);
				break;
			case "double" : 
				readDouble(row, content, item.getName(), errorCatcher, excelName);
				break;
			}
		}
		return true;
	}
	
	private void readInt(int row, String content, String column, ErrorCatcher errorCatcher, String excelName) {
		try {
			Integer.parseInt(content);
		} catch (Exception e) {
			errorCatcher.catchError(excelName, row, column, content + " 不能转为int.");
		}
	}
	
	private void readArrayInt(int row, String content, String column, ErrorCatcher errorCatcher, String excelName) {
		String[] array = content.split("&&");
		for (int k = 0;k < array.length;k++) {
			try {
				Integer.parseInt(array[k]);
			} catch (Exception e) {
				errorCatcher.catchError(excelName, row, column, array[k] + " 不能转为int数组.(k=" + k + "), 内容是 " + content + ".");
			}
		}
	}
	
	private void readDouble(int row, String content, String column, ErrorCatcher errorCatcher, String excelName) {
		try {
			Double.parseDouble(content);
		} catch (Exception e) {
			errorCatcher.catchError(excelName, row, column, content + " 不能转为double.");
		}
	}

	@Override
	public void rowFinish() {}

	@Override
	protected void excelBegin() {}

	@Override
	protected void rowBegin() {}

	@Override
	protected void excelFinsihCall() {}

}
