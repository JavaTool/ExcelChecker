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

public final class RelationExcelFinder extends BaseRelationFinder implements RelationSupplier {
	
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
