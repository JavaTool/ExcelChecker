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

/**
 * YC Excel数据结构组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public final class YCExcel extends BaseExcel {

	@Override
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
	
	/**
	 * 读取Excel列信息
	 * @param 	clientFieldNames
	 * 			客户端名称行
	 * @param 	fieldTypes
	 * 			数据类型行
	 * @param 	dataIndex
	 * 			索引信息行
	 * @param 	serverFieldNames
	 * 			服务器名称行
	 * @param 	errorCatcher
	 * 			错误捕获组件
	 */
	private void readItems(Row clientFieldNames, Row fieldTypes, Row dataIndex, Row serverFieldNames, ErrorCatcher errorCatcher) {
		int size = lastColumn - firstColumn;
		items = new ExcelItem[size];
		Map<String, Integer> names = Maps.newHashMap();
		// 组装列信息
		for (int i = firstColumn, index = 0;i < lastColumn;i++, index++) {
			String name = getName(readCellAsString(clientFieldNames.getCell(i)), readCellAsString(serverFieldNames.getCell(i)), errorCatcher);
			if (name == null) {
				continue;
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
		// 检测实际数据末行
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

	/**
	 * 检测客户端名称和服务器名称，尝试获取列名称
	 * @param 	clientName
	 * 			客户端名称
	 * @param 	serverName
	 * 			服务器名称
	 * @param 	errorCatcher
	 * 			错误捕获组件
	 * @return	列名称(失败时为空)
	 */
	private String getName(String clientName, String serverName, ErrorCatcher errorCatcher) {
		if (Strings.isNullOrEmpty(clientName) && Strings.isNullOrEmpty(serverName)) {
			return null;
		} else if (Strings.isNullOrEmpty(serverName)) {
			return clientName;
		} else if (Strings.isNullOrEmpty(clientName)) {
			return serverName;
		} else if (!clientName.equals(serverName)) {
			errorCatcher.catchError(excelName, 6, serverName, "列名不一致.");
			return null;
		} else {
			return serverName;
		}
	}
	
	@Override
	public void checkData(final ExcelsData excelsData) {
		// Excel检测开始
		for (ContentChecker checker : checkers) {
			checker.excelBegin(this);
		}
		// 读取所有行
		readEachRow(new RowScaner() {
			
			@Override
			public void scan(Row row) {
				// 行检测开始
				for (ContentChecker checker : checkers) {
					checker.rowBegin(row.getRowNum() + 1);
				}
				// 读取所有单元格
				for (int j = firstColumn, index = 0;j < lastColumn;j++, index++) {
					if (items[index] == null || row == null) {
						continue;
					}
					String content = items[index] == null || row == null ? "" : readCellAsString(row.getCell(j));
					// 检测单元格内容
					for (ContentChecker checker : checkers) {
						if (!checker.check(content, items[index], excelsData.getErrorCatcher())) {
							break;
						}
					}
				}
				// 行检测结束
				for (ContentChecker checker : checkers) {
					checker.rowFinish();
				}
			}
			
		});
		// Excel检测结束
		for (ContentChecker checker : checkers) {
			checker.excelFinsih();
		}
	}

}
