package tool.checker.excel.checker;

import com.google.common.base.Strings;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelRelation;
import tool.checker.excel.ExcelsData;
import tool.checker.excel.finder.RelationFinder;
import tool.checker.excel.function.ErrorCatcher;

public final class RelationChecker extends BaseContentChecker {

	@Override
	public boolean check(String content, ExcelItem item, ExcelsData excelsData) {
		if (Strings.isNullOrEmpty(content)) {
			return true;
		}
		String name = item.getName();
		RelationFinder relationFinder = excelsData.getClassToInstanceMap().getInstance(RelationFinder.class);
		String excelName = excel.getExcelName();
		ExcelRelation excelRelation = relationFinder.getRelation(excelName, name);
		if (excelRelation == null) {
			return true;
		}
		String otherExcelName = excelRelation.getExcel();
		String foreign = excelRelation.getForeign();
		ErrorCatcher errorCatcher = excelsData.getErrorCatcher();
		SetMultimap<String, String> refKeys = relationFinder.getRefKeys(otherExcelName);
		switch (item.getType().toLowerCase()) {
		case "array_int" : 
		case "array_string" : 
			String[] array = content.split("&&");
			for (int k = 0;k < array.length;k++) {
				if (!refKeys.get(foreign).contains(array[k])) {
					errorCatcher.catchError(excelName, row, name, "找不到关系 [" + array[k] + "] 到表 " + otherExcelName + " 's " + foreign + ".");
				}
			}
			break;
		default :
			if (!refKeys.get(foreign).contains(content)) {
				errorCatcher.catchError(excelName, row, name, "找不到关系 [" + content + "] 到表 " + otherExcelName + " 's " + foreign + ".");
			}
		}
		return true;
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
