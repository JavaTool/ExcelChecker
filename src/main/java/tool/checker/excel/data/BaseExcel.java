package tool.checker.excel.data;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.function.RowScaner;

/**
 * Excel数据结构组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public abstract class BaseExcel {
	
	/** 数据首行 */
	protected int firstRow;
	/** 数据首列 */
	protected int firstColumn;
	/** 数据末行 */
	protected int lastRow;
	/** 数据末列 */
	protected int lastColumn;
	/** 表名称 */
	protected String excelName;
	/** 列信息数组 */
	protected ExcelItem[] items;
	/** 列名称与列信息数组索引集合 */
	protected Map<String, Integer> colums = Maps.newHashMap();
	/** 检测组件集合 */
	protected List<ContentChecker> checkers = Lists.newLinkedList();
	/** 被检测的Sheet */
	protected Sheet sheet;
	
	/**
	 * 加载Excel
	 * @param 	sheet
	 * 			被检测的Sheet
	 * @param 	errorCatcher
	 * 			错误捕获组件
	 */
	public abstract void loadExcel(Sheet sheet, ErrorCatcher errorCatcher);
	
	/**
	 * 检测数据
	 * @param 	excelsData
	 * 			Excel整体数据
	 */
	public abstract void checkData(final ExcelsData excelsData);
	
	/**
	 * 设置Excel名称
	 * @param 	excelName
	 * 			Excel名称
	 */
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	
	/**
	 * 获取Excel名称
	 * @return	Excel名称
	 */
	public String getExcelName() {
		return excelName;
	}
	
	/**
	 * 添加检测组件
	 * @param 	checker
	 * 			检测组件
	 */
	public void addChecker(ContentChecker checker) {
		checkers.add(checker);
	}
	
	/**
	 * 读取所有行
	 * @param 	rowScaner
	 * 			行扫描器
	 */
	public void readEachRow(RowScaner rowScaner) {
		for (int i = firstRow;i < lastRow;i++) {
			rowScaner.scan(sheet.getRow(i));
		}
	}
	
	/**
	 * 通过列名称获取列信息
	 * @param 	columnName
	 * 			列名称
	 * @return	列信息
	 */
	public ExcelItem getExcelItem(String columnName) {
		return colums.containsKey(columnName) ? items[colums.get(columnName)] : null;
	}

}
