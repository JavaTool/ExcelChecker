package tool.checker.excel.checker;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelsData;
import tool.checker.excel.function.ErrorCatcher;

public final class PrimaryChecker implements ContentChecker {
	
	private String excelName;
	
	private int row = 0;
	
	private boolean hasPrimary = false;
	
	private final Set<String> primaryKeys = Sets.newHashSet();

	@Override
	public boolean check(Excel excel, int row, String content, ExcelItem item, ExcelsData excelsData) {
		// 新表
		if (!excel.getExcelName().equals(excelName)) {
			excelName = excel.getExcelName();
			primaryKeys.clear();
			this.row = 0;
		}
		if (this.row != row) {
			this.row = row;
			hasPrimary = false;
		}
		// 检测主键列唯一性
		ErrorCatcher errorCatcher = excelsData.getErrorCatcher();
		if ("primary".equalsIgnoreCase(item.getIndex())) {
			if (hasPrimary) {
				errorCatcher.catchError(excelName, row, item.getName(), "重复的主键列 [" + item.getName() + "].");
				return false;
			} else {
				hasPrimary = true;
			}
			readPrimary(row, content, primaryKeys, item.getName(), errorCatcher);
		}
		return true;
	}
	
	private void readPrimary(int row, String content, Set<String> primaryKeys, String column, ErrorCatcher errorCatcher) {
		try {
			Preconditions.checkArgument(primaryKeys.add(content), "主键 [%s] 重复.", content);
		} catch (IllegalArgumentException e) {
			String error = e.getMessage();
			errorCatcher.catchError(excelName, row, column, (Strings.isNullOrEmpty(error) ? content + " 不能转为int." : error));
		}
	}

}
