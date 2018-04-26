package tool.checker.excel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

public class Excel {
	
	private int firstRow;
	
	private int firstColumn;
	
	private int lastRow;
	
	private int lastColumn;
	
	private String excelName;
	
	private ExcelItem[] items;
	
	private Map<String, Integer> colums = Maps.newHashMap();
	
	private Map<String, ExcelRelation> relations = Maps.newHashMap();
	
	private SetMultimap<String, String> refKeys = HashMultimap.create();
	
	private List<Integer> refs = Lists.newLinkedList();
	
	private boolean isLoad;
	
	public void addRelations(String name, ExcelRelation relation) {
		relations.put(name, relation);
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
	
	public void loadExcel(Sheet sheet, Set<String> itemNames, ErrorCatcher errorCatcher) {
		if (isLoad) {
			return;
		}
		Row range = sheet.getRow(0);
		Row fieldTypes = sheet.getRow(3);
		Row dataIndex = sheet.getRow(4);
		Row fieldNames = sheet.getRow(5);
		if (fieldNames == null || dataIndex == null || fieldTypes == null) {
			System.out.println("忽略表 " + excelName);
			return;
		}
		
		try {
			firstRow = Integer.parseInt(readCellAsString(range.getCell(0))) - 1;
			firstColumn = Integer.parseInt(readCellAsString(range.getCell(1))) - 1;
			lastRow = Math.min(Integer.parseInt(readCellAsString(range.getCell(2))), sheet.getLastRowNum()) + 1;
			lastColumn = Integer.parseInt(readCellAsString(range.getCell(3)));
		} catch (Exception e) {
			e.printStackTrace();
			errorCatcher.catchError(excelName, 1, "", "读取范围错误。");
			return;
		}
		
		int size = lastColumn - firstColumn;
		items = new ExcelItem[size];
		Map<String, Integer> names = Maps.newHashMap();
		for (int i = firstColumn, index = 0;i < lastColumn;i++, index++) {
			String name = readCellAsString(fieldNames.getCell(i));
			if (Strings.isNullOrEmpty(name)) {
				continue;
			}
			items[index] = new ExcelItem();
			items[index].setType(readCellAsString(fieldTypes.getCell(i)));
			items[index].setIndex(readCellAsString(dataIndex.getCell(i)));
			if (names.containsKey(name)) {
				errorCatcher.catchError(excelName, 6, name, "列名称与第" + names.get(name) + "列重复.");
			} else {
				names.put(name, i);
			}
			items[index].setName(name);
			items[index].setColum(i);
			colums.put(name, index);
			if (itemNames != null && itemNames.contains(items[index].getName())) {
				refs.add(index);
			}
		}
		
		isLoad = true;
	}
	
	public void loadRefs(Sheet sheet) {
		for (int i = firstRow;i < lastRow;i++) {
			Row row = sheet.getRow(i);
			for (Integer colum : refs) {
				String content = readCellAsString(row.getCell(items[colum].getColum()));
				refKeys.put(items[colum].getName(), content);
			}
		}
	}
	
	public void checkData(Sheet sheet, ErrorCatcher errorCatcher, Function<String, Excel> supplier) {
		Set<String> primaryKeys = Sets.newHashSet();
		String[] contents = new String[lastColumn - firstColumn];
		for (int i = firstRow;i < lastRow;i++) {
			Row row = sheet.getRow(i);
			int blank = 0;
			for (int j = firstColumn, index = 0;j < lastColumn;j++, index++) {
				contents[index] = items[index] == null || row == null ? "" : readCellAsString(row.getCell(j));
				blank += Strings.isNullOrEmpty(contents[index]) ? 1 : 0;
			}
			if (blank == contents.length) {
				break;
			}
			boolean hasPrimary = false;
			for (int j = firstColumn, index = 0;j < lastColumn;j++, index++) {
				if (items[index] == null || row == null) {
					continue;
				}
				String content = contents[index];
				if ("primary".equalsIgnoreCase(items[index].getIndex())) {
					if (hasPrimary) {
						errorCatcher.catchError(excelName, i, items[index].getName(), "重复的主键列 [" + items[index].getName() + "].");
						continue;
					} else {
						hasPrimary = true;
					}
					readPrimary(i, content, primaryKeys, items[index].getName(), errorCatcher);
				} else if (!Strings.isNullOrEmpty(content)) {
					switch (items[index].getType().toLowerCase()) {
					case "int" : 
						readInt(i, content, items[index].getName(), errorCatcher);
						break;
					case "array_int" : 
						readArrayInt(i, content, items[index].getName(), errorCatcher);
						break;
					case "double" : 
						readDouble(i, content, items[index].getName(), errorCatcher);
						break;
					}
					checkForeign(items[index], i, content, supplier, errorCatcher);
				}
			}
		}
	}
	
	private void checkForeign(ExcelItem item, int row, String content, Function<String, Excel> supplier, ErrorCatcher errorCatcher) {
		String name = item.getName();
		if (relations.containsKey(name)) {
			ExcelRelation excelRelation = relations.get(name);
			String otherExcel = excelRelation.getExcel();
			Excel excel = supplier.apply(otherExcel);
			String foreign = excelRelation.getForeign();
			switch (item.getType().toLowerCase()) {
			case "array_int" : 
			case "array_string" : 
				String[] array = content.split("&&");
				for (int k = 0;k < array.length;k++) {
					if (!excel.refKeys.get(foreign).contains(array[k])) {
						errorCatcher.catchError(excelName, row + 1, name, "找不到关系 [" + array[k] + "] 到表 " + otherExcel + " 's " + foreign + ".");
					}
				}
				break;
			default :
				if (!excel.refKeys.get(foreign).contains(content)) {
					errorCatcher.catchError(excelName, row + 1, name, "找不到关系 [" + content + "] 到表 " + otherExcel + " 's " + foreign + ".");
				}
			}
		}
	}
	
	private void readInt(int row, String content, String column, ErrorCatcher errorCatcher) {
		try {
			Integer.parseInt(content);
		} catch (Exception e) {
			errorCatcher.catchError(excelName, row + 1, column, content + " 不能转为int.");
		}
	}
	
	private void readArrayInt(int row, String content, String column, ErrorCatcher errorCatcher) {
		String[] array = content.split("&&");
		for (int k = 0;k < array.length;k++) {
			try {
				Integer.parseInt(array[k]);
			} catch (Exception e) {
				errorCatcher.catchError(excelName, row + 1, column, array[k] + " 不能转为int数组.(k=" + k + "), 内容是 " + content + ".");
			}
		}
	}
	
	private void readDouble(int row, String content, String column, ErrorCatcher errorCatcher) {
		try {
			Double.parseDouble(content);
		} catch (Exception e) {
			errorCatcher.catchError(excelName, row + 1, column, content + " 不能转为double.");
		}
	}
	
	private void readPrimary(int row, String content, Set<String> primaryKeys, String column, ErrorCatcher errorCatcher) {
		try {
			Preconditions.checkArgument(primaryKeys.add(content), "主键 [%s] 重复.", content);
		} catch (IllegalArgumentException e) {
			String error = e.getMessage();
			errorCatcher.catchError(excelName, row + 1, column, (Strings.isNullOrEmpty(error) ? content + " 不能转为int." : error));
		}
	}

	public int getFirstRow() {
		return firstRow;
	}

	public int getFirstColumn() {
		return firstColumn;
	}

	public int getLastRow() {
		return lastRow;
	}

	public int getLastColumn() {
		return lastColumn;
	}

	public Map<String, ExcelRelation> getRelations() {
		return relations;
	}
	
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public boolean isLoad() {
		return isLoad;
	}

}
