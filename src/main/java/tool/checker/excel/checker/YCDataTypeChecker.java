package tool.checker.excel.checker;

import com.google.common.base.Strings;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;

/**
 * YC 数据类型规范检测组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public final class YCDataTypeChecker extends BaseContentChecker {

	@Override
	public boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher) {
		if (!Strings.isNullOrEmpty(content)) {
			switch (item.getType().toLowerCase()) {
			case "int" : 
				checkInt(content, item.getName(), errorCatcher);
				break;
			case "array_int" : 
				checkArrayInt(content, item.getName(), errorCatcher);
				break;
			case "double" : 
				checkDouble(content, item.getName(), errorCatcher);
				break;
			}
		}
		return true;
	}
	
	/**
	 * 检测Int
	 * @param 	content
	 * 			检测内容
	 * @param 	column
	 * 			列名称
	 * @param 	errorCatcher
	 * 			错误捕获组件
	 */
	private void checkInt(String content, String column, ErrorCatcher errorCatcher) {
		try {
			Integer.parseInt(content);
		} catch (Exception e) {
			errorCatcher.catchError(excel.getExcelName(), row, column, content + " 不能转为int.");
		}
	}
	
	/**
	 * 检测Int数组
	 * @param 	content
	 * 			检测内容
	 * @param 	column
	 * 			列名称
	 * @param 	errorCatcher
	 * 			错误捕获组件
	 */
	private void checkArrayInt(String content, String column, ErrorCatcher errorCatcher) {
		String[] array = content.split("&&", -2);
		for (int k = 0;k < array.length;k++) {
			try {
				Integer.parseInt(array[k]);
			} catch (Exception e) {
				errorCatcher.catchError(excel.getExcelName(), row, column, array[k] + " 不能转为int数组.(k=" + k + "), 内容是 " + content + ".");
			}
		}
	}
	
	/**
	 * 检测Double
	 * @param 	content
	 * 			检测内容
	 * @param 	column
	 * 			列名称
	 * @param 	errorCatcher
	 * 			错误捕获组件
	 */
	private void checkDouble(String content, String column, ErrorCatcher errorCatcher) {
		try {
			Double.parseDouble(content);
		} catch (Exception e) {
			errorCatcher.catchError(excel.getExcelName(), row, column, content + " 不能转为double.");
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
