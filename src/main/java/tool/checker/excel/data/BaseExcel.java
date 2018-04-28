package tool.checker.excel.data;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.function.RowScaner;

public abstract class BaseExcel {
	
	protected int firstRow;
	
	protected int firstColumn;
	
	protected int lastRow;
	
	protected int lastColumn;
	
	protected String excelName;
	
	protected ExcelItem[] items;
	
	protected Map<String, Integer> colums = Maps.newHashMap();
	
	protected List<ContentChecker> checkers = Lists.newLinkedList();
	
	protected Sheet sheet;
	
	public abstract void loadExcel(Sheet sheet, ErrorCatcher errorCatcher);
	
	public abstract void checkData(final ExcelsData excelsData);
	
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	
	public String getExcelName() {
		return excelName;
	}
	
	public void addChecker(ContentChecker checker) {
		checkers.add(checker);
	}
	
	public void readEachRow(RowScaner rowScaner) {
		for (int i = firstRow;i < lastRow;i++) {
			rowScaner.scan(sheet.getRow(i));
		}
	}
	
	public ExcelItem getExcelItem(String columnName) {
		return colums.containsKey(columnName) ? items[colums.get(columnName)] : null;
	}

}
