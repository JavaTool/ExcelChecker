package tool.checker.excel.checker;

import java.util.Map;

import com.google.common.collect.Maps;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.data.ExcelsData;
import tool.checker.excel.finder.ArrayFinder;
import tool.checker.excel.function.DataConsumer;

@DataConsumer({@DataConsumer.Type(value=ArrayFinder.class)})
public final class YunChangArrayChecker extends BaseContentChecker {
	
	private final Map<String, Integer> lengths = Maps.newHashMap();

	@Override
	public boolean check(String content, ExcelItem item, ExcelsData excelsData) {
		String excelName = excel.getExcelName();
		String column = item.getName();
		String key = excelsData.getClassToInstanceMap().getInstance(ArrayFinder.class).getArrayGroup(excelName, column);
		if (key == null) {
			return true;
		}
		
		if (lengths.containsKey(key)) {
			if (content.split("&&").length != lengths.get(key)) {
				excelsData.getErrorCatcher().catchError(excelName, row, column, "数组列[" + key + "]长度不同。");
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
