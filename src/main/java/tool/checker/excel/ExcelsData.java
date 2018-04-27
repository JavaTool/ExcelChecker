package tool.checker.excel;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class ExcelsData {
	
	private File dir;
	
	private Map<String, Excel> excels = Maps.newHashMap();
	
	private Map<String, File> files = Maps.newHashMap();
	
	private Function<String, String> configs;
	
	private ErrorCatcher errorCatcher;

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

}
