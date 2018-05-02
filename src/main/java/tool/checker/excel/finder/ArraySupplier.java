package tool.checker.excel.finder;

import tool.checker.excel.function.DataSupplier;

public interface ArraySupplier extends DataSupplier {
	
	String getArrayGroup(String excelName, String column);

}
