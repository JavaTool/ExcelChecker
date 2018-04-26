package tool.checker.excel.checker;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;

import tool.checker.excel.ErrorCatcher;
import tool.checker.excel.Excel;

public interface Checker {
	
	void check(Map<String, File> loadFiles, Function<String, Excel> supplier, ErrorCatcher errorCatcher);

}
