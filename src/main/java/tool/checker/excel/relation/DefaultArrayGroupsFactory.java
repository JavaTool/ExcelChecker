package tool.checker.excel.relation;

import java.io.File;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.Utils;

public class DefaultArrayGroupsFactory implements ArrayGroupsFactory {

	@Override
	public SetMultimap<String, String> loadArrayGroup(File dir, Function<String, String> configs) {
		SetMultimap<String, String> setMap = HashMultimap.create();
		try {
			Workbook config = Utils.createWorkbook(new File(dir, configs.apply("relationPath")));
			Sheet sheet = config.getSheet("arrayGroups");
			for (int i = 1;i <= sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				String excelName = row.getCell(0).getStringCellValue();
				String columArray = row.getCell(1).getStringCellValue();
				setMap.put(excelName, columArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return setMap;
	}

}
