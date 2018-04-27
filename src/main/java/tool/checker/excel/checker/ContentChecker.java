package tool.checker.excel.checker;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelsData;

public interface ContentChecker {
	
	boolean check(Excel excel, int row, String content, ExcelItem item, ExcelsData excelsData);

}
