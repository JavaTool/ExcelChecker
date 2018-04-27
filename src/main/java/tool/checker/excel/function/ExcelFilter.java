package tool.checker.excel.function;

import java.io.File;
import java.io.FileFilter;

import tool.checker.excel.Utils;

public final class ExcelFilter implements FileFilter {
	
	public static final FileFilter EXCEL_FILTER = new ExcelFilter();
	
	private ExcelFilter() {}

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() || Utils.isExcelFile(pathname.getName());
	}

}
