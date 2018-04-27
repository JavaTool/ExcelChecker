package tool.checker.excel.relation;

import java.io.File;

import com.google.common.base.Function;
import com.google.common.collect.SetMultimap;

public interface ArrayGroupsFactory {
	
	SetMultimap<String, String> loadArrayGroup(File dir, Function<String, String> configs);

}
