package tool.checker.excel.relation;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.ErrorCatcher;
import tool.checker.excel.Excel;

public interface RefLoader {
	
	void loadRefs(Function<String, String> configs, Map<String, File> loadFiles, Function<String, Excel> supplier, SetMultimap<String, String> refKeys, ErrorCatcher errorCatcher);

}
