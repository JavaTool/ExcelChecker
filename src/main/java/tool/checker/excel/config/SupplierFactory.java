package tool.checker.excel.config;

import com.google.common.base.Function;

import tool.checker.excel.Excel;

public interface SupplierFactory {
	
	Function<String, Excel> createSupplier();

}
