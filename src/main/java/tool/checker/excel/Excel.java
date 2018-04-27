package tool.checker.excel;

import static tool.checker.excel.Utils.readCellAsString;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.checker.ContentChecker;

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
	
	private Map<String, String> arrayGroups = Maps.newHashMap();
	
	private List<ContentChecker> checkers = Lists.newLinkedList();
	
	private Sheet sheet;
	
	private boolean isLoad;
	
	public void addRelations(String name, ExcelRelation relation) {
		relations.put(name, relation);
	}
	
	public void addArray(String array) {
		for (String column : array.split(",")) {
			arrayGroups.put(column, array);
		}
	}
	
	public void loadExcel(Sheet sheet, ErrorCatcher errorCatcher) {
		if (isLoad) {
			return;
		}
		
		this.sheet = sheet;
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
		}
		
		isLoad = true;
	}
	
	public void loadRefs(Set<String> itemNames) {
		for (int i = firstRow;i < lastRow;i++) {
			Row row = sheet.getRow(i);
			for (String columnName : itemNames) {
				int index = colums.get(columnName);
				if (items[index] != null && itemNames.contains(items[index].getName())) {
					for (Integer colum : refs) {
						String content = readCellAsString(row.getCell(items[colum].getColum()));
						refKeys.put(items[colum].getName(), content);
					}
				}
			}
		}
	}
	
	public void checkData(ExcelsData excelsData) {
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
			for (int j = firstColumn, index = 0;j < lastColumn;j++, index++) {
				if (items[index] == null || row == null) {
					continue;
				}
				String content = contents[index];
				for (ContentChecker checker : checkers) {
					if (!checker.check(this, i, content, items[index], excelsData)) {
						break;
					}
				}
			}
		}
	}

	public Map<String, ExcelRelation> getRelations() {
		return relations;
	}
	
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	
	public String getExcelName() {
		return excelName;
	}

	public boolean isLoad() {
		return isLoad;
	}
	
	public void addChecker(ContentChecker checker) {
		checkers.add(checker);
	}
	
	public SetMultimap<String, String> getRefKeys() {
		return refKeys;
	}

}
