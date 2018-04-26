package tool.checker.excel.relation;

import java.io.File;

import com.google.common.base.Function;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.Excel;

public interface RelationLoader {
	
	SetMultimap<String, String> loadConfig(File dir, Function<String, String> configs, Function<String, Excel> supplier);

}
