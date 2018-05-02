package tool.checker.excel.data;

import static tool.checker.excel.Utils.readCellAsString;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.function.RowScaner;

public class YunChangExcel extends BaseExcel {
	
	public void loadExcel(Sheet sheet, ErrorCatcher errorCatcher) {
		if (this.sheet != null) {
			return;
		}
		
		this.sheet = sheet;

		Row range = sheet.getRow(0);
		Row clientFieldNames = sheet.getRow(2);
		Row fieldTypes = sheet.getRow(3);
		Row dataIndex = sheet.getRow(4);
		Row serverFieldNames = sheet.getRow(5);
		if (clientFieldNames == null || serverFieldNames == null || dataIndex == null || fieldTypes == null) {
			errorCatcher.catchError(excelName, "表头信息不对，忽略。");
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

		readItems(clientFieldNames, fieldTypes, dataIndex, serverFieldNames, errorCatcher);
	}
	
	private void readItems(Row clientFieldNames, Row fieldTypes, Row dataIndex, Row serverFieldNames, ErrorCatcher errorCatcher) {
		int size = lastColumn - firstColumn;
		items = new ExcelItem[size];
		Map<String, Integer> names = Maps.newHashMap();
		for (int i = firstColumn, index = 0;i < lastColumn;i++, index++) {
			String clientName = readCellAsString(clientFieldNames.getCell(i));
			String serverName = readCellAsString(serverFieldNames.getCell(i));
			String name;
			if (Strings.isNullOrEmpty(clientName) && Strings.isNullOrEmpty(serverName)) {
				continue;
			} else if (Strings.isNullOrEmpty(serverName)) {
				name = clientName;
			} else if (Strings.isNullOrEmpty(clientName)) {
				name = serverName;
			} else if (!clientName.equals(serverName)) {
				errorCatcher.catchError(excelName, 6, serverName, "列名不一致.");
				continue;
			} else {
				name = serverName;
			}
			items[index] = new ExcelItem();
			items[index].setType(readCellAsString(fieldTypes.getCell(i)));
			items[index].putAttribute("index", readCellAsString(dataIndex.getCell(i)));
			if (names.containsKey(name)) {
				errorCatcher.catchError(excelName, 6, name, "列名称与第" + names.get(name) + "列重复.");
			} else {
				names.put(name, i);
			}
			items[index].setName(name);
			items[index].setColum(i);
			colums.put(name, index);
		}

		String[] contents = new String[lastColumn - firstColumn];
		for (int i = firstRow;i < lastRow;i++) {
			Row row = sheet.getRow(i);
			int blank = 0;
			for (int j = firstColumn, index = 0;j < lastColumn;j++, index++) {
				contents[index] = items[index] == null || row == null ? "" : readCellAsString(row.getCell(j));
				blank += Strings.isNullOrEmpty(contents[index]) ? 1 : 0;
			}
			if (blank == contents.length) {
				lastRow = i;
				break;
			}
		}
	}
	
	public void checkData(final ExcelsData excelsData) {
		for (ContentChecker checker : checkers) {
			checker.excelBegin(this);
		}
		readEachRow(new RowScaner() {
			
			@Override
			public void scan(Row row) {
				for (ContentChecker checker : checkers) {
					checker.rowBegin(row.getRowNum() + 1);
				}
				for (int j = firstColumn, index = 0;j < lastColumn;j++, index++) {
					if (items[index] == null || row == null) {
						continue;
					}
					String content = items[index] == null || row == null ? "" : readCellAsString(row.getCell(j));
					for (ContentChecker checker : checkers) {
						if (!checker.check(content, items[index], excelsData.getErrorCatcher())) {
							break;
						}
					}
				}
				for (ContentChecker checker : checkers) {
					checker.rowFinish();
				}
			}
			
		});
		for (ContentChecker checker : checkers) {
			checker.excelFinsih();
		}
	}

}
