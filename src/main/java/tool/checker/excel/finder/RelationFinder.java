package tool.checker.excel.finder;

import com.google.common.collect.SetMultimap;

import tool.checker.excel.function.DataSupplier;

public interface RelationFinder extends DataSupplier {
	
	ExcelRelation getRelation(String excelName, String column);
	
	SetMultimap<String, String> getRefKeys(String excelName);

}
