package tool.checker.excel.error;

import java.util.List;

/**
 * 错误收集组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface ErrorCatcher {

	/**
	 * 捕获错误信息
	 * @param 	error
	 * 			错误信息
	 */
	void catchError(String error);

	/**
	 * 捕获错误信息
	 * @param 	excelName
	 * 			Excel名称
	 * @param 	error
	 * 			错误信息
	 */
	void catchError(String excelName, String error);
	
	/**
	 * 捕获错误信息
	 * @param 	excelName
	 * 			Excel名称
	 * @param 	row
	 * 			行号
	 * @param 	columnName
	 * 			列名称
	 * @param 	error
	 * 			错误信息
	 */
	void catchError(String excelName, int row, String columnName, String error);
	
	/**
	 * 获取所有错误信息
	 * @return	错误信息列表
	 */
	List<String> getErrors();

}
