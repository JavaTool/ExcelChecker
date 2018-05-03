package tool.checker.excel.finder;

import tool.checker.excel.function.DataSupplier;

/**
 * 数组数据提供组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface ArraySupplier extends DataSupplier {
	
	/**
	 * 获取数组列组名称
	 * @param 	excelName
	 * 			Excel名称
	 * @param 	column
	 * 			列名称
	 * @return	列组名称
	 */
	String getArrayGroup(String excelName, String column);

}
