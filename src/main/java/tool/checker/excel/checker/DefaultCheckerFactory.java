package tool.checker.excel.checker;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Function;

import tool.checker.excel.ErrorCatcher;
import tool.checker.excel.Excel;
import tool.checker.excel.Utils;

public class DefaultCheckerFactory implements CheckerFactory {

	@Override
	public Checker createChecker() {
		return new Checker() {
			
			@Override
			public void check(Map<String, File> loadFiles, Function<String, Excel> supplier, ErrorCatcher errorCatcher) {
				for (File file : loadFiles.values()) {
					checkFile(file, supplier, errorCatcher);
				}
			}
			
		};
	}
	
	private static void checkFile(File file, Function<String, Excel> supplier, ErrorCatcher errorCatcher) {
		if (file.isFile()) {
			String excelName = file.getName();
			try {
				Workbook wb = Utils.createWorkbook(file);
				Excel excel = supplier.apply(excelName);
				Sheet sheet = wb.getSheetAt(0);
				excel.setExcelName(excelName);
				excel.loadExcel(sheet, null, errorCatcher);
				if (!excel.isLoad()) {
					return;
				}
				excel.checkData(sheet, errorCatcher, supplier);
				System.out.println("加载被检测表 : " + excelName);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("加载被检测表 : " + excelName + " 错误");
			}
		} else {
			for (File subFile : file.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory() || Utils.isExcelFile(pathname.getName());
				}
				
			})) {
				checkFile(subFile, supplier, errorCatcher);
			}
		}
	}

}
