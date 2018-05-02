package tool.checker.excel.checker;

import java.util.Map;

import com.google.common.collect.Maps;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.ArraySupplier;
import tool.checker.excel.function.DataConsumer;

@DataConsumer({@DataConsumer.Type(value=ArraySupplier.class)})
public final class YunChangArrayChecker extends BaseContentChecker {
	
	private final Map<String, Integer> lengths = Maps.newHashMap();

	@Override
	public boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher) {
		String excelName = excel.getExcelName();
		String column = item.getName();
		String key = getDataSupplier(ArraySupplier.class).getArrayGroup(excelName, column);
		if (key == null) {
			return true;
		}
		
		if (lengths.containsKey(key)) {
			if (content.split("&&").length != lengths.get(key)) {
				errorCatcher.catchError(excelName, row, column, "数组列[" + key + "]长度不同。");
			}
		} else {
			lengths.put(key, content.split("&&").length);
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
