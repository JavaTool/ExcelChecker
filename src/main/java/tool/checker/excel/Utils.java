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

/**
 * 工具类
 * @author fuhuiyuan
 * @since 1.0.0
 */
public class Utils {
	
	/**
	 * 判断是否为有效Excel文件名
	 * @param 	fileName
	 * 			Excel文件名
	 * @return	是否有效
	 */
	public static boolean isExcelFile(String fileName) {
		return (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) && checkFileName(fileName);
	}
	
	/**
	 * 获取无后缀的Excel文件名
	 * @param 	fileName
	 * 			Excel文件名
	 * @return	无后缀的Excel文件名
	 */
	public static String getFileName(String fileName) {
		return fileName.replace(".xlsx", "").replace(".xls", "");
	}
	
	/**
	 * 检测文件名是否合法
	 * @param 	fileName
	 * 			文件名
	 * @return	是否合法
	 */
	public static boolean checkFileName(String fileName) {
		return !fileName.startsWith("~$");
	}
	
	/**
	 * 创建Excel对象
	 * @param 	file
	 * 			Excel文件
	 * @return	Excel对象
	 * @throws 	Exception
	 */
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
	
	/**
	 * 查找并加载文件
	 * @param 	file
	 * 			目录
	 * @param 	fileFilter
	 * 			文件过滤器
	 * @param 	callback
	 * 			加载过程
	 */
	public static void findLoadFiles(File file, FileFilter fileFilter, Callback<File> callback) {
		if (file.isFile()) {
			callback.callback(file);
		} else {
			for (File subFile : file.listFiles(fileFilter)) {
				findLoadFiles(subFile, fileFilter, callback);
			}
		}
	}
	
	/**
	 * 创建Excel数据组件
	 * @param 	file
	 * 			Excel文件
	 * @param 	excelsData
	 * 			Excel整体数据
	 * @return	Excel数据组件
	 */
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

	/**
	 * 以字符串读取Excel单元格内容
	 * @param 	cell
	 * 			Excel单元格
	 * @return	单元格内容
	 */
	public static String readCellAsString(Cell cell) {
		return cell == null ? "" : readCellAsString(cell, true, cell.getCellType());
	}
	
	/**
	 * 以字符串读取Excel单元格内容
	 * @param 	cell
	 * 			Excel单元格
	 * @param 	isInt
	 * 			数值是否强制为int
	 * @param 	cellType
	 * 			单元格数据类型
	 * @return	单元格内容
	 */
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
	
	/**
	 * 读取文件中每行的内容到键值对集合
	 * @param 	file
	 * 			文件
	 * @return	键值对集合
	 */
	public static Map<String, String> readLines(File file) {
		Map<String, String> map = Maps.newHashMap();
		readLines(file, map);
		return map;
	}
	
	/**
	 * 读取文件中每行的内容
	 * @param 	file
	 * 			文件
	 * @param 	lineReader
	 * 			行读取器
	 */
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
	
	/**
	 * 读取文件中每行的键值对
	 * @param 	file
	 * 			文件
	 * @param 	map
	 * 			键值对集合
	 */
	public static void readLines(File file, final Map<String, String> map) {
		readLines(file, new LineReader() {
			
			@Override
			public void readLine(int lineNumber, String line) {
				saveKeyValue(line, map);
			}
			
		});
	}
	
	/**
	 * 保存一行键值对
	 * @param 	line
	 * 			行内容
	 * @param 	map
	 * 			键值对集合
	 */
	public static void saveKeyValue(String line, Map<String, String> map) {
		String[] infos = line.split("=", -2);
		map.put(infos[0].trim(), infos[1].trim());
	}

}
