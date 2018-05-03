package tool.checker.excel.checker;

import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.function.DataSupplier;

/**
 * 内容检测组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface ContentChecker {
	
	/**
	 * Excel检测结束处理
	 * @param 	excel
	 * 			Excel数据
	 */
	void excelBegin(BaseExcel excel);
	
	/**
	 * 行检测开始处理
	 * @param 	row
	 * 			行号
	 */
	void rowBegin(int row);

	/**
	 * 单元格内容检测
	 * @param 	content
	 * 			单元格内容
	 * @param 	item
	 * 			列属性
	 * @param 	errorCatcher
	 * 			错误收集组件
	 * @return	是否可以继续检测
	 */
	boolean check(String content, ExcelItem item, ErrorCatcher errorCatcher);

	/**
	 * 行检测结束处理
	 */
	void rowFinish();
	
	/**
	 * Excel检测结束处理
	 */
	void excelFinsih();
	
	/**
	 * 添加数据提供组件
	 * @param 	clz
	 * 			数据提供组件类型
	 * @param 	suplier
	 * 			数据提供组件
	 */
	<T extends DataSupplier> void addDataSuplier(Class<T> clz, DataSupplier suplier);

}
