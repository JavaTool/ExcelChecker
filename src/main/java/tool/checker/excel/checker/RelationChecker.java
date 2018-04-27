package tool.checker.excel.checker;

import java.util.Map;

import com.google.common.collect.SetMultimap;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelRelation;
import tool.checker.excel.ExcelsData;
import tool.checker.excel.function.ErrorCatcher;

public class RelationChecker implements ContentChecker {

	@Override
	public boolean check(Excel excel, int row, String content, ExcelItem item, ExcelsData excelsData) {
		String name = item.getName();
		Map<String, ExcelRelation> relations = excel.getRelations();
		if (relations.containsKey(name)) {
			ExcelRelation excelRelation = relations.get(name);
			String otherExcelName = excelRelation.getExcel();
			Excel otherExcel = excelsData.getExcels().get(otherExcelName);
			String foreign = excelRelation.getForeign();
			ErrorCatcher errorCatcher = excelsData.getErrorCatcher();
			String excelName = excel.getExcelName();
			SetMultimap<String, String> refKeys = otherExcel.getRefKeys();
			switch (item.getType().toLowerCase()) {
			case "array_int" : 
			case "array_string" : 
				String[] array = content.split("&&");
				for (int k = 0;k < array.length;k++) {
					if (!refKeys.get(foreign).contains(array[k])) {
						errorCatcher.catchError(excelName, row + 1, name, "找不到关系 [" + array[k] + "] 到表 " + otherExcelName + " 's " + foreign + ".");
					}
				}
				break;
			default :
				if (!refKeys.get(foreign).contains(content)) {
					errorCatcher.catchError(excelName, row + 1, name, "找不到关系 [" + content + "] 到表 " + otherExcelName + " 's " + foreign + ".");
				}
			}
		}
		return true;
	}

}
