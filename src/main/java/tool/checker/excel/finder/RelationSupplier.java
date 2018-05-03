package tool.checker.excel.finder;

import com.google.common.collect.SetMultimap;

import tool.checker.excel.function.DataSupplier;

/**
 * 引用关系数据提供组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface RelationSupplier extends DataSupplier {
	
	/**
	 * 获取引用关系
	 * @param 	excelName
	 * 			Excel名称
	 * @param 	column
	 * 			列名称
	 * @return	引用关系
	 */
	ExcelRelation getRelation(String excelName, String column);
	
	/**
	 * 获取引用表内容集合
	 * @param 	excelName
	 * 			Excel名称
	 * @return	引用表内容集合[key=列名称，value=内容集合]
	 */
	SetMultimap<String, String> getRefKeys(String excelName);

}
