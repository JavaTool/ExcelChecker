package tool.checker.excel.checker;

import tool.checker.excel.BaseExcel;
import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelsData;

public interface ContentChecker {
	
	void excelBegin(BaseExcel excel);
	
	void rowBegin(int row);
	
	boolean check(String content, ExcelItem item, ExcelsData excelsData);
	
	void rowFinish();
	
	void excelFinsih();

}
