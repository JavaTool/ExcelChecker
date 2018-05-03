package tool.checker.excel.output;

import tool.checker.excel.data.ExcelsData;

/**
 * 输出组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface Outputer {
	
	/**
	 * 输出
	 * @param 	excelsData
	 * 			Excel整体数据
	 */
	void out(ExcelsData excelsData);

}
