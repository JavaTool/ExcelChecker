package tool.checker.excel.config;

import tool.checker.excel.data.ExcelsData;

public interface ConfigLoader {
	
	String KEY_ALL_PATH = "allPath";
	
	String KEY_IGNORE = "ignore";
	
	String KEY_PATHS = "paths";
	
	String KEY_RELATION_PATH = "relationPath";
	
	String KEY_OUT_PATH = "outPath";
	
	String VALUE_ALL_PATH = "D:\\workspace\\data\\excel";
	
	String VALUE_IGNORE = "";
	
	String VALUE_PATHS = "";
	
	String VALUE_RELATION_PATH = "ExcelCheckerConfig.xlsx";
	
	String VALUE_OUT_PATH = "excel_check_result.txt";
	
	void load(ExcelsData excelsData);

}
