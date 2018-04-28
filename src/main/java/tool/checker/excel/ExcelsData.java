package tool.checker.excel;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.Maps;
import com.google.common.collect.MutableClassToInstanceMap;

import tool.checker.excel.finder.ExcelFinder;
import tool.checker.excel.function.ErrorCatcher;

public class ExcelsData {
	
	private File dir;
	
	private Map<String, Excel> excels = Maps.newHashMap();
	
	private Map<String, File> files = Maps.newHashMap();
	
	private Function<String, String> configs;
	
	private ErrorCatcher errorCatcher;
	
	private ClassToInstanceMap<ExcelFinder> classToInstanceMap = MutableClassToInstanceMap.create();

	public Map<String, Excel> getExcels() {
		return excels;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public Function<String, String> getConfigs() {
		return configs;
	}

	public void setConfigs(Function<String, String> configs) {
		this.configs = configs;
	}

	public ErrorCatcher getErrorCatcher() {
		return errorCatcher;
	}

	public void setErrorCatcher(ErrorCatcher errorCatcher) {
		this.errorCatcher = errorCatcher;
	}

	public Map<String, File> getFiles() {
		return files;
	}

	public void setFiles(Map<String, File> files) {
		this.files = files;
	}

	public ClassToInstanceMap<ExcelFinder> getClassToInstanceMap() {
		return classToInstanceMap;
	}

}
