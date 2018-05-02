package tool.checker.excel.finder;

import static tool.checker.excel.Utils.readCellAsString;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.Utils;
import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelItem;
import tool.checker.excel.data.ExcelsData;
import tool.checker.excel.function.RowScaner;

public abstract class BaseRelationFinder implements ExcelFinder {

	@Override
	public void find(ExcelsData excelsData) {
		start();
		Function<String, String> configs = excelsData.getConfigs();
		String relationPath = configs.apply("relationPath");
		SetMultimap<String, String> refKeys = HashMultimap.create();
		try {
			Workbook config = Utils.createWorkbook(new File(excelsData.getDir(), relationPath));
			Sheet sheet = config.getSheetAt(0);
			Map<String, BaseExcel> excels = excelsData.getExcels();
			Map<String, File> files = excelsData.getFiles();
			for (int i = 1;i <= sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				String excelName = row.getCell(0).getStringCellValue();
				String columName = row.getCell(1).getStringCellValue();
				BaseExcel excel = excels.get(excelName);
				String otherExcel = Utils.readCellAsString(row.getCell(2));
				String foreign = row.getCell(3).getStringCellValue();
				if (Strings.isNullOrEmpty(otherExcel)) {
					saveRelation(excelName, columName, "", foreign);
				} else if (excel == null) { // 未加载的非指定检测表
					// 依赖指定检测的表
					if (excels.containsKey(otherExcel) && files.containsKey(excelName)) {
						excel = Utils.createExcel(files.get(excelName), excelsData);
						excels.put(excelName, excel);
						System.out.println("加载影响引用的文件[" + excelName + "]");
					} else { // 不依赖指定检测的表，跳过
						continue;
					}
				} else if (!files.containsKey(excelName)) { // 加载的指定检测表
					// 加载未加载的依赖表
					if (!excels.containsKey(otherExcel)) {
						excels.put(otherExcel, Utils.createExcel(files.get(otherExcel), excelsData));
						System.out.println("加载需要引用的文件[" + otherExcel + "]");
					}
					saveRelation(excelName, columName, otherExcel, foreign);
					refKeys.put(otherExcel, foreign);
				}
			}
			for (String excelName : refKeys.keySet()) {
				loadRefs(excels.get(excelName), refKeys.get(excelName));
			}
			finish(excelsData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void start();
	
	protected abstract void saveRelation(String excelName, String columName, String otherExcel, String foreign);
	
	protected abstract void finish(ExcelsData excelsData);
	
	private void loadRefs(final BaseExcel excel, final Set<String> itemNames) {
		final SetMultimap<String, String> refKeys = HashMultimap.create();
		excel.readEachRow(new RowScaner() {
			
			@Override
			public void scan(Row row) {
				for (String columnName : itemNames) {
					ExcelItem item = excel.getExcelItem(columnName);
					if (item != null) {
						String content = readCellAsString(row.getCell(item.getColum()));
						refKeys.put(columnName, content);
					}
				}
			}
			
		});
		saveRefs(excel, refKeys);
	}
	
	protected abstract void saveRefs(BaseExcel excel, SetMultimap<String, String> refKeys);

}