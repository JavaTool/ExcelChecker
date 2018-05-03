package tool.checker.excel.finder;

import tool.checker.excel.data.ExcelsData;

/**
 * Excel查询组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface ExcelFinder {
	
	/**
	 * 寻找需要的数据
	 * @param 	excelsData
	 * 			Excel整体数据
	 */
	void find(ExcelsData excelsData);

}
