package tool.checker.excel.checker;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.EnumAndRelationSupplier;
import tool.checker.excel.finder.RelationSupplier;
import tool.checker.excel.function.DataConsumer;

/**
 * 支持枚举检测的关系检测组件
 * @author fuhuiyuan
 * @since 2.0.0
 */
@DataConsumer({@DataConsumer.Type(value=RelationSupplier.class), @DataConsumer.Type(value=EnumAndRelationSupplier.class)})
public final class SupportEnumRelationChecker extends BaseRelationChecker {

	@Override
	protected void checkIfExcelNull(String content, ExcelItem item, String foreign, ErrorCatcher errorCatcher) {
		EnumAndRelationSupplier enumAndRelationSupplier = getDataSupplier(EnumAndRelationSupplier.class);
		String excelName = excel.getExcelName();
		String itemName = item.getName();
		switch (item.getType().toLowerCase()) {
		case "array_int" : 
		case "array_string" : 
			String[] array = content.split("&&");
			for (String info : array) {
				if (!enumAndRelationSupplier.containsEnum(foreign, info)) {
					errorCatcher.catchError(excelName, row, itemName, "枚举[" + foreign + "]中不存在[" + info + "].");
				}
			}
			break;
		default :
			if (!enumAndRelationSupplier.containsEnum(foreign, content)) {
				errorCatcher.catchError(excelName, row, itemName, "枚举[" + foreign + "]中不存在[" + content + "].");
			}
		}
	}

}
