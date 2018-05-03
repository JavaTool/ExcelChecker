package tool.checker.excel.finder;

import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;

import tool.checker.excel.Utils;
import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelsData;

/**
 * 引用关系查询组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public final class RelationExcelFinder extends BaseRelationFinder implements RelationSupplier {
	
	/** 关系集合[row=表名称，column=列名称，value=关系数据] */
	private final Table<String, String, ExcelRelation> relations = HashBasedTable.create();
	/** 引用集合[key=被引用的表名称，value=引用列集合[key=列名称，value=内容集合]] */
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
	protected void saveRelation(String excelName, String columName, String otherExcel, String foreign) {
		relations.put(excelName, columName, new ExcelRelation(otherExcel, foreign));
	}

	@Override
	protected void finish(ExcelsData excelsData) {
		excelsData.getDataSuppliers().putInstance(RelationSupplier.class, this);
	}

	@Override
	protected void start() {
		relations.clear();
	}

	@Override
	protected void saveRefs(BaseExcel excel, SetMultimap<String, String> refKeys) {
		refKeyMap.put(Utils.getFileName(excel.getExcelName()), ImmutableSetMultimap.copyOf(refKeys));
	}

}
