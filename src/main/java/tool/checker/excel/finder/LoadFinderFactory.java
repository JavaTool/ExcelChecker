package tool.checker.excel.finder;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;

public interface LoadFinderFactory {
	
	Function<Function<String, String>, Map<String, File>> createLoadFinder();

}
