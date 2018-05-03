package tool.checker.excel.checker;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;

/**
 * 云畅游戏 主键检测组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public final class YunChangPrimaryChecker extends BaseContentChecker {
	
	/** 是否已经存在主键 */
	private boolean hasPrimary = false;
	/** 主键值集合 */
	private final Set<String> primaryKeys = Sets.newHashSet();

	@Override
	public boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher) {
		// 检测主键列唯一性
		if ("primary".equalsIgnoreCase(item.getAttribute("index"))) {
			if (hasPrimary) {
				errorCatcher.catchError(excel.getExcelName(), row, item.getName(), "重复的主键列 [" + item.getName() + "].");
				return false;
			} else {
				hasPrimary = true;
			}
			readPrimary(content, primaryKeys, item.getName(), errorCatcher);
		}
		return true;
	}
	
	/**
	 * 检测主键
	 * @param 	content
	 * 			检测内容
	 * @param 	primaryKeys
	 * 			主键集合
	 * @param 	column
	 * 			列名称
	 * @param 	errorCatcher
	 * 			错误捕获组件
	 */
	private void readPrimary(String content, Set<String> primaryKeys, String column, ErrorCatcher errorCatcher) {
		try {
			Preconditions.checkArgument(primaryKeys.add(content), "主键 [%s] 重复.", content);
		} catch (IllegalArgumentException e) {
			String error = e.getMessage();
			errorCatcher.catchError(excel.getExcelName(), row, column, (Strings.isNullOrEmpty(error) ? content + " 不能转为int." : error));
		}
	}

	@Override
	public void rowFinish() {}

	@Override
	protected void excelBegin() {
		primaryKeys.clear();
	}

	@Override
	protected void rowBegin() {
		hasPrimary = false;
	}

	@Override
	protected void excelFinsihCall() {}

}
