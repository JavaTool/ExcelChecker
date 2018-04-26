package tool.checker.excel;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Utils {
	
	public static boolean isExcelFile(String fileName) {
		return (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) && checkFileName(fileName);
	}
	
	public static String getFileName(String fileName) {
		return fileName.replace(".xlsx", "").replace(".xls", "");
	}
	
	public static boolean checkFileName(String fileName) {
		return !fileName.startsWith("~$");
	}
	
	public static Workbook createWorkbook(File file) throws Exception {
		if (file.getName().endsWith(".xls")) {
			try {
				return new HSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				return WorkbookFactory.create(new FileInputStream(file));
			}
		} else {
			return WorkbookFactory.create(new FileInputStream(file));
		}
	}

}
