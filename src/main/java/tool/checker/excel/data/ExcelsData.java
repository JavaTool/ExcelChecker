package tool.checker.excel.data;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.Maps;
import com.google.common.collect.MutableClassToInstanceMap;

import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.function.DataSupplier;

public class ExcelsData {
	
	private File dir;
	
	private Map<String, BaseExcel> excels = Maps.newHashMap();
	
	private Map<String, File> files = Maps.newHashMap();
	
	private Function<String, String> configs;
	
	private ErrorCatcher errorCatcher;
	
	private ClassToInstanceMap<DataSupplier> classToInstanceMap = MutableClassToInstanceMap.create();
	
	private Class<BaseExcel> excelClass;

	public Map<String, BaseExcel> getExcels() {
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

	public ClassToInstanceMap<DataSupplier> getClassToInstanceMap() {
		return classToInstanceMap;
	}

	public Class<BaseExcel> getExcelClass() {
		return excelClass;
	}

	public void setExcelClass(Class<BaseExcel> excelClass) {
		this.excelClass = excelClass;
	}

}
