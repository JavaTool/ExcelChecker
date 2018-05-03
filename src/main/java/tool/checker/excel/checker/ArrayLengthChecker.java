package tool.checker.excel.checker;

import java.util.Map;

import com.google.common.collect.Maps;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.ArraySupplier;
import tool.checker.excel.function.DataConsumer;

/**
 * 数组长度检测组件，数组分隔符为 && 。
 * @author fuhuiyuan
 * @since 1.0.0
 */
@DataConsumer({@DataConsumer.Type(value=ArraySupplier.class)})
public final class ArrayLengthChecker extends BaseContentChecker {
	
	/** 数组长度记录[key=列组名称，value=长度] */
	private final Map<String, Integer> lengths = Maps.newHashMap();

	@Override
	public boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher) {
		String excelName = excel.getExcelName();
		String column = item.getName();
		// 查找列组名称
		String key = getDataSupplier(ArraySupplier.class).getArrayGroup(excelName, column);
		if (key == null) {
			return true;
		}
		// 检测
		if (lengths.containsKey(key)) {
			if (content.split("&&", -2).length != lengths.get(key)) {
				errorCatcher.catchError(excelName, row, column, "数组列[" + key + "]长度不同。");
			}
		} else { // 首次检测该列祖
			lengths.put(key, content.split("&&", -2).length);
		}
		return true;
	}

	@Override
	public void rowFinish() {}

	@Override
	protected void excelBegin() {}

	@Override
	protected void rowBegin() {
		lengths.clear();
	}

	@Override
	protected void excelFinsihCall() {}

}
