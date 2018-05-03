package tool.checker.excel.config;

import tool.checker.excel.data.ExcelsData;

/**
 * 配置加载组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface ConfigLoader {
	
	/** 配置键：全部Excel目录 */
	String KEY_ALL_PATH = "allPath";
	/** 配置键：忽略的文件 */
	String KEY_IGNORE = "ignore";
	/** 配置键：加载的子文件或目录 */
	String KEY_PATHS = "paths";
	/** 配置键：关系表相对路径 */
	String KEY_RELATION_PATH = "relationPath";
	/** 配置键：输出相对路径 */
	String KEY_OUT_PATH = "outPath";
	/** 默认值：全部Excel目录 */
	String VALUE_ALL_PATH = "D:\\workspace\\data\\excel";
	/** 默认值：忽略的文件 */
	String VALUE_IGNORE = "";
	/** 默认值：加载的子文件或目录 */
	String VALUE_PATHS = "";
	/** 默认值：关系表相对路径 */
	String VALUE_RELATION_PATH = "ExcelCheckerConfig.xlsx";
	/** 默认值：输出相对路径 */
	String VALUE_OUT_PATH = "excel_check_result.txt";
	
	/**
	 * 加载
	 * @param 	excelsData
	 * 			Excel整体数据
	 */
	void load(ExcelsData excelsData);

}
