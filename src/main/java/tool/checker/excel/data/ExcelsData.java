package tool.checker.excel.data;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.Maps;
import com.google.common.collect.MutableClassToInstanceMap;

import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.function.DataSupplier;

/**
 * Excel整体数据
 * @author fuhuiyuan
 * @since 1.0.0
 */
public class ExcelsData {
	
	/** 工作目录 */
	private File dir;
	/** Excel数据集合 */
	private Map<String, BaseExcel> excels = Maps.newHashMap();
	/** 文件集合 */
	private Map<String, File> files = Maps.newHashMap();
	/** 配置获取组件 */
	private Function<String, String> configs;
	/** 错误信息捕获组件 */
	private ErrorCatcher errorCatcher;
	/** 数据提供组件集合 */
	private ClassToInstanceMap<DataSupplier> dataSupliers = MutableClassToInstanceMap.create();
	/** Excel数据结构组件类 */
	private Class<BaseExcel> excelClass;

	/**
	 * 获取Excel数据集合
	 * @return	Excel数据集合
	 */
	public Map<String, BaseExcel> getExcels() {
		return excels;
	}

	/**
	 * 获取工作目录
	 * @return	工作目录
	 */
	public File getDir() {
		return dir;
	}

	/**
	 * 设置工作目录
	 * @param 	dir
	 * 			工作目录
	 */
	public void setDir(File dir) {
		this.dir = dir;
	}

	/**
	 * 获取配置获取组件
	 * @return	配置获取组件
	 */
	public Function<String, String> getConfigs() {
		return configs;
	}

	/**
	 * 设置配置获取组件
	 * @param 	configs
	 * 			配置获取组件
	 */
	public void setConfigs(Function<String, String> configs) {
		this.configs = configs;
	}

	/**
	 * 获取错误信息捕获组件
	 * @return	错误信息捕获组件
	 */
	public ErrorCatcher getErrorCatcher() {
		return errorCatcher;
	}

	/**
	 * 设置错误信息捕获组件
	 * @param 	errorCatcher
	 * 			错误信息捕获组件
	 */
	public void setErrorCatcher(ErrorCatcher errorCatcher) {
		this.errorCatcher = errorCatcher;
	}

	/**
	 * 获取文件集合
	 * @return	文件集合
	 */
	public Map<String, File> getFiles() {
		return files;
	}

	/**
	 * 获取数据提供组件集合
	 * @return	数据提供组件集合
	 */
	public ClassToInstanceMap<DataSupplier> getDataSuppliers() {
		return dataSupliers;
	}

	/**
	 * 获取Excel数据结构组件类
	 * @return	Excel数据结构组件类
	 */
	public Class<BaseExcel> getExcelClass() {
		return excelClass;
	}

	/**
	 * 设置Excel数据结构组件类
	 * @param 	excelClass
	 * 			Excel数据结构组件类
	 */
	public void setExcelClass(Class<BaseExcel> excelClass) {
		this.excelClass = excelClass;
	}

}
