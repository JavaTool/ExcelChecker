package tool.checker.excel.finder;

import static tool.checker.excel.Utils.readCellAsString;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Function;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelItem;
import tool.checker.excel.ExcelsData;
import tool.checker.excel.Utils;
import tool.checker.excel.function.RowScaner;

public final class RelationExcelFinder implements RelationFinder, ExcelFinder {
	
	private final Table<String, String, ExcelRelation> relations = HashBasedTable.create();
	
	private final Map<String, SetMultimap<String, String>> refKeyMap = Maps.newHashMap();

	@Override
	public ExcelRelation getRelation(String excelName, String column) {
		return relations.get(Utils.getFileName(excelName), column);
	}

	@Override
	public SetMultimap<String, String> getRefKeys(String excelName) {
		return refKeyMap.get(Utils.getFileName(excelName));
	}

	@Override
	public void find(ExcelsData excelsData) {
		relations.clear();
		Function<String, String> configs = excelsData.getConfigs();
		String relationPath = configs.apply("relationPath");
		SetMultimap<String, String> refKeys = HashMultimap.create();
		try {
			Workbook config = Utils.createWorkbook(new File(excelsData.getDir(), relationPath));
			Sheet sheet = config.getSheetAt(0);
			Map<String, Excel> excels = excelsData.getExcels();
			Map<String, File> files = excelsData.getFiles();
			for (int i = 1;i <= sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				String excelName = row.getCell(0).getStringCellValue();
				String columName = row.getCell(1).getStringCellValue();
				Excel excel = excels.get(excelName);
				String otherExcel = row.getCell(2).getStringCellValue();
				// 未加载的非指定检测表
				if (excel == null) {
					// 依赖指定检测的表
					if (excels.containsKey(otherExcel) && files.containsKey(excel)) {
						excel = Utils.createExcel(files.get(excel), excelsData.getErrorCatcher());
						excels.put(excelName, excel);
					} else { // 不依赖指定检测的表，跳过
						continue;
					}
				} else if (!files.containsKey(excel)) { // 加载的指定检测表
					// 加载未加载的依赖表
					if (!excels.containsKey(otherExcel)) {
						excels.put(otherExcel, Utils.createExcel(files.get(otherExcel), excelsData.getErrorCatcher()));
					}
				}
				String foreign = row.getCell(3).getStringCellValue();
				relations.put(excelName, columName, new ExcelRelation(otherExcel, foreign));
				refKeys.put(otherExcel, foreign);
			}
			for (String excelName : refKeys.keySet()) {
				Excel excel = excels.get(excelName);
				loadRefs(excel, refKeys.get(excelName));
			}
			excelsData.getClassToInstanceMap().putInstance(RelationFinder.class, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadRefs(final Excel excel, final Set<String> itemNames) {
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
		refKeyMap.put(Utils.getFileName(excel.getExcelName()), ImmutableSetMultimap.copyOf(refKeys));
	}

}
