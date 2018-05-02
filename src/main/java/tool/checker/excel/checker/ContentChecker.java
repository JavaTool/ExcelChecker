package tool.checker.excel.checker;

import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.function.DataSupplier;

public interface ContentChecker {
	
	void excelBegin(BaseExcel excel);
	
	void rowBegin(int row);
	
	boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher);
	
	void rowFinish();
	
	void excelFinsih();
	
	<T extends DataSupplier> void addDataSuplier(Class<T> clz, DataSupplier suplier);

}
