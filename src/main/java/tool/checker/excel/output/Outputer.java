package tool.checker.excel.output;

import java.io.File;
import java.util.Collection;

import com.google.common.base.Function;

public interface Outputer {
	
	void out(File dir, Function<String, String> configs, Collection<String> errors);

}
