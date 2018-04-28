package tool.checker.excel.checker;

import java.util.Map;

import com.google.common.collect.Maps;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelsData;
import tool.checker.excel.finder.ArrayFinder;

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
		String key = excelsData.getClassToInstanceMap().getInstance(ArrayFinder.class).getArrayGroup(content, column);
		if (key == null) {
			return true;
		}
		
		if (lengths.containsKey(key)) {
			if (content.split("&&").length != lengths.get(key)) {
				excelsData.getErrorCatcher().catchError(excelName, row + 1, column, "数组列[" + key + "]长度不同。");
			}
		} else {
			lengths.put(key, content.split("&&").length);
		}
		return true;
	}

}
