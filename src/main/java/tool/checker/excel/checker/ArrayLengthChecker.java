package tool.checker.excel.checker;

import java.util.Map;

import com.google.common.collect.Maps;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelsData;

public final class ArrayLengthChecker implements ContentChecker {
	
	private String excelName;
	
	private int row = 0;
	
	private final Map<String, Integer> lengths = Maps.newHashMap();

	@Override
	public boolean check(Excel excel, int row, String content, ExcelItem item, ExcelsData excelsData) {
		// 新表
		if (!excel.getExcelName().equals(excelName)) {
			excelName = excel.getExcelName();
			this.row = 0;
		}
		if (this.row != row) {
			this.row = row;
			lengths.clear();
		}
		
		String column = item.getName();
		String key = excel.getArrayGroups().get(column);
		if (key == null) {
			return true;
		}
		
		if (lengths.containsKey(key)) {
			if (content.split("&&").length != lengths.get(key)) {
				excelsData.getErrorCatcher().catchError(excelName, row + 1, column, "数组长度不同[" + column + "]在组" + key + "中。");
			}
		} else {
			lengths.put(key, content.split("&&").length);
		}
		return true;
	}

}
