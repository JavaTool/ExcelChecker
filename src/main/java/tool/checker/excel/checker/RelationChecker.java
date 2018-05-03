package tool.checker.excel.checker;

import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.RelationSupplier;
import tool.checker.excel.function.DataConsumer;

/**
 * 关系检测组件，仅检测表之间的引用关系。
 * @author fuhuiyuan
 * @since 1.0.0
 */
@DataConsumer({@DataConsumer.Type(value=RelationSupplier.class)})
public final class RelationChecker extends BaseRelationChecker {

	@Override
	protected void checkIfExcelNull(String content, ExcelItem item, String foreign, ErrorCatcher errorCatcher) {
		errorCatcher.catchError(excel.getExcelName(), row, item.getName(), "表关系配置的引用表名为空。");
	}

}
