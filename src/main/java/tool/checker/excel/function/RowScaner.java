package tool.checker.excel.function;

import org.apache.poi.ss.usermodel.Row;

/**
 * Excel行扫描器
 * @author fuhuiyuan
 * @since 1.0.0
 */
public interface RowScaner {
	
	/**
	 * 扫描
	 * @param 	row
	 * 			行数据
	 */
	void scan(Row row);

}
