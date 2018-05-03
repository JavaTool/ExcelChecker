package tool.checker.excel.function;

import java.io.File;
import java.io.FileFilter;

import tool.checker.excel.Utils;

/**
 * Excel文件过滤器
 * @author fuhuiyuan
 * @since 1.0.0
 */
public final class ExcelFilter implements FileFilter {
	
	/** 单例 */
	public static final FileFilter EXCEL_FILTER = new ExcelFilter();
	
	private ExcelFilter() {}

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() || Utils.isExcelFile(pathname.getName());
	}

}
