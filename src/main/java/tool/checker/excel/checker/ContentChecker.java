package tool.checker.excel.checker;

import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.data.ExcelsData;

public interface ContentChecker {
	
	void excelBegin(BaseExcel excel);
	
	void rowBegin(int row);
	
	boolean check(String content, ExcelItem item, ExcelsData excelsData);
	
	void rowFinish();
	
	void excelFinsih();

}
