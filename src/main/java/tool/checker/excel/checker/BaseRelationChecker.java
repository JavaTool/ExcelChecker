package tool.checker.excel.checker;

import com.google.common.base.Strings;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.ExcelRelation;
import tool.checker.excel.finder.RelationSupplier;

public abstract class BaseRelationChecker extends BaseContentChecker {

	@Override
	public final boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher) {
		if (Strings.isNullOrEmpty(content)) {
			return true;
		}
		String name = item.getName();
		RelationSupplier relationFinder = getDataSupplier(RelationSupplier.class);
		String excelName = excel.getExcelName();
		ExcelRelation excelRelation = relationFinder.getRelation(excelName, name);
		if (excelRelation == null) {
			return true;
		}
		String otherExcelName = excelRelation.getExcel();
		String foreign = excelRelation.getForeign();
		if (Strings.isNullOrEmpty(otherExcelName)) {
			checkIfExcelNull(content, item, foreign, errorCatcher);
		} else {
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
		}
		return true;
	}
	
	protected abstract void checkIfExcelNull(String content, ExcelItem item, String foreign, ErrorCatcher errorCatcher);

	@Override
	public void rowFinish() {}

	@Override
	protected void excelBegin() {}

	@Override
	protected void rowBegin() {}

	@Override
	protected void excelFinsihCall() {}

}
