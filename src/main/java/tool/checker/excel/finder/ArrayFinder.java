package tool.checker.excel.finder;

import tool.checker.excel.function.DataSupplier;

public interface ArrayFinder extends DataSupplier {
	
	String getArrayGroup(String excelName, String column);

}
