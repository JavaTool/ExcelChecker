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
		switch (item.getType().toLowerCase()) {
		case "array_int" : 
		case "array_string" : 
			String[] array = content.split("&&");
			for (int k = 0;k < array.length;k++) {
				if (!getDataSupplier(EnumAndRelationSupplier.class).containsEnum(foreign, array[k])) {
					errorCatcher.catchError(excel.getExcelName(), row, item.getName(), "枚举[" + foreign + "]中不存在[" + array[k] + "].");
				}
			}
			break;
		default :
			if (!getDataSupplier(EnumAndRelationSupplier.class).containsEnum(foreign, content)) {
				errorCatcher.catchError(excel.getExcelName(), row, item.getName(), "枚举[" + foreign + "]中不存在[" + content + "].");
			}
		}
	}

}
