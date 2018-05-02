package tool.checker.excel;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.Maps;

import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelsData;
import tool.checker.excel.function.Callback;
import tool.checker.excel.function.LineReader;

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
	
	public static void findLoadFiles(File file, FileFilter fileFilter, Callback<File> callback) {
		if (file.isFile()) {
			callback.callback(file);
		} else {
			for (File subFile : file.listFiles(fileFilter)) {
				findLoadFiles(subFile, fileFilter, callback);
			}
		}
	}
	
	public static BaseExcel createExcel(File file, ExcelsData excelsData) {
		try {
			BaseExcel excel = excelsData.getExcelClass().newInstance();
			excel.setExcelName(file.getName());
			excel.loadExcel(Utils.createWorkbook(file).getSheetAt(0), excelsData.getErrorCatcher());
			return excel;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	public static String readCellAsString(Cell cell) {
		return cell == null ? "" : readCellAsString(cell, true, cell.getCellType());
	}
	
	public static String readCellAsString(Cell cell, boolean isInt, int cellType) {
		if (cell == null) {
			return "";
		}
		switch (cellType) {
		case Cell.CELL_TYPE_BLANK : 
			return "";
		case Cell.CELL_TYPE_BOOLEAN : 
			return cell.getBooleanCellValue() ? "true" : "false";
		case Cell.CELL_TYPE_ERROR : 
			return cell.getErrorCellValue() + "";
		case Cell.CELL_TYPE_FORMULA : 
			return readCellAsString(cell, isInt, cell.getCachedFormulaResultType());
		case Cell.CELL_TYPE_NUMERIC : 
			if (isInt) {
				return ((int) cell.getNumericCellValue()) + "";
			}
			return (cell.getNumericCellValue() + "").replace(".0", "");
		case Cell.CELL_TYPE_STRING : 
			return cell.getStringCellValue();
		default : 
			throw new IllegalArgumentException("Unknow cell type : " + cell.getCellType() + ".");
		}
	}
	
	public static Map<String, String> readLines(File file) {
		Map<String, String> map = Maps.newHashMap();
		readLines(file, map);
		return map;
	}
	
	public static void readLines(File file, LineReader lineReader) {
		try (LineNumberReader pathReader = new LineNumberReader(new FileReader(file))) {
			int number = 0;
			String line;
			while ((line = pathReader.readLine()) != null) {
				lineReader.readLine(number++, line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void readLines(File file, final Map<String, String> map) {
		readLines(file, new LineReader() {
			
			@Override
			public void readLine(int lineNumber, String line) {
				saveKeyValue(line, map);
			}
			
		});
	}
	
	public static void saveKeyValue(String line, Map<String, String> map) {
		String[] infos = line.split("=", -2);
		map.put(infos[0].trim(), infos[1].trim());
	}

}
