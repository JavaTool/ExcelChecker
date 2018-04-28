package tool.checker.excel.finder;

import java.io.File;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelsData;
import tool.checker.excel.Utils;

public final class ExcelArrayFinder implements ArrayFinder, ExcelFinder {
	
	private final Table<String, String, String> arrayGroups = HashBasedTable.create();

	@Override
	public String getArrayGroup(String excelName, String column) {
		return arrayGroups.get(Utils.getFileName(excelName), column);
	}

	@Override
	public void find(ExcelsData excelsData) {
		arrayGroups.clear();
		try {
			Workbook config = Utils.createWorkbook(new File(excelsData.getDir(), excelsData.getConfigs().apply("relationPath")));
			Sheet sheet = config.getSheet("arrayGroups");
			Map<String, Excel> excels = excelsData.getExcels();
			for (int i = 1;i <= sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				String excelName = row.getCell(0).getStringCellValue();
				String columArray = row.getCell(1).getStringCellValue();
				Excel excel = excels.get(excelName);
				if (excel != null) {
					addArray(excelName, columArray);
				}
			}
			excelsData.getClassToInstanceMap().putInstance(ArrayFinder.class, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addArray(String excelName, String array) {
		for (String column : array.split(",")) {
			arrayGroups.put(excelName, column, array);
		}
	}

}
