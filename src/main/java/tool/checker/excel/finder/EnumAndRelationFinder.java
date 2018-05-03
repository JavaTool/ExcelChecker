package tool.checker.excel.finder;

import java.io.File;
import java.util.Map;

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

import tool.checker.excel.Utils;
import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelsData;

/**
 * 枚举和引用关系查询组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public final class EnumAndRelationFinder extends BaseRelationFinder implements EnumAndRelationSupplier {
	
	/** 关系集合[row=表名称，column=列名称，value=关系数据] */
	private final Table<String, String, ExcelRelation> relations = HashBasedTable.create();
	/** 引用集合[key=被引用的表名称，value=引用列集合[key=列名称，value=内容集合]] */
	private final Map<String, SetMultimap<String, String>> refKeyMap = Maps.newHashMap();
	/** 枚举集合[key=枚举名称，value=内容集合] */
	private final SetMultimap<String, String> enums = HashMultimap.create();

	@Override
	public ExcelRelation getRelation(String excelName, String column) {
		return relations.get(Utils.getFileName(excelName), column);
	}

	@Override
	public SetMultimap<String, String> getRefKeys(String excelName) {
		return refKeyMap.get(Utils.getFileName(excelName));
	}

	@Override
	protected void saveRelation(String excelName, String columName, String otherExcel, String foreign) {
		relations.put(excelName, columName, new ExcelRelation(otherExcel, foreign));
	}

	@Override
	protected void finish(ExcelsData excelsData) {
		Function<String, String> configs = excelsData.getConfigs();
		String relationPath = configs.apply("relationPath");
		try {
			Workbook config = Utils.createWorkbook(new File(excelsData.getDir(), relationPath));
			Sheet sheet = config.getSheet("enums");
			for (int i = 1;i <= sheet.getLastRowNum();i++) {
				Row row = sheet.getRow(i);
				String type = row.getCell(0).getStringCellValue();
				String content = Utils.readCellAsString(row.getCell(1));
				enums.put(type, content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		excelsData.getDataSuppliers().putInstance(RelationSupplier.class, this);
		excelsData.getDataSuppliers().putInstance(EnumAndRelationSupplier.class, this);
	}

	@Override
	protected void start() {
		relations.clear();
		enums.clear();
	}

	@Override
	protected void saveRefs(BaseExcel excel, SetMultimap<String, String> refKeys) {
		refKeyMap.put(Utils.getFileName(excel.getExcelName()), ImmutableSetMultimap.copyOf(refKeys));
	}

	@Override
	public boolean containsEnum(String type, String content) {
		return enums.get(type).contains(content);
	}

}
