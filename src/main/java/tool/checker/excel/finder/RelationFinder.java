package tool.checker.excel.finder;

import com.google.common.collect.SetMultimap;

import tool.checker.excel.ExcelRelation;

public interface RelationFinder extends ExcelFinder {
	
	ExcelRelation getRelation(String excelName, String column);
	
	SetMultimap<String, String> getRefKeys(String excelName);

}
