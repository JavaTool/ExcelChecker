package tool.checker.excel.relation;

import java.io.File;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelRelation;
import tool.checker.excel.Utils;

public final class DefaultRelationLoaderFactory implements RelationLoaderFactory {

	@Override
	public RelationLoader createRelationLoader() {
		return new RelationLoader() {
			
			@Override
			public SetMultimap<String, String> loadConfig(File dir, Function<String, String> configs, Function<String, Excel> supplier) {
				SetMultimap<String, String> refKeys = HashMultimap.create();
				try {
					Workbook config = Utils.createWorkbook(new File(dir, configs.apply("relationPath")));
					Sheet relationSheet = config.getSheetAt(0);
					for (int i = 1;i <= relationSheet.getLastRowNum();i++) {
						Row row = relationSheet.getRow(i);
						String excelName = row.getCell(0).getStringCellValue();
						String columName = row.getCell(1).getStringCellValue();
						Excel excel = supplier.apply(excelName);
						if (excel == null) {
							continue;
						}
						String otherExcel = row.getCell(2).getStringCellValue();
						String foreign = row.getCell(3).getStringCellValue();
						excel.addRelations(columName, new ExcelRelation(otherExcel, foreign));
						refKeys.put(otherExcel, foreign);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				return refKeys;
			}
			
		};
	}

}
