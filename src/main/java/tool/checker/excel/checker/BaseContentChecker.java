package tool.checker.excel.checker;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.function.DataSupplier;

public abstract class BaseContentChecker implements ContentChecker {
	
	protected BaseExcel excel;
	
	protected int row;
	
	private ClassToInstanceMap<DataSupplier> dataSupliers = MutableClassToInstanceMap.create();

	@Override
	public void excelBegin(BaseExcel excel) {
		this.excel = excel;
		excelBegin();
	}

	@Override
	public void rowBegin(int row) {
		this.row = row;
		rowBegin();
	}

	@Override
	public void excelFinsih() {
		excelFinsihCall();
		excel = null;
	}
	
	protected abstract void excelBegin();
	
	protected abstract void rowBegin();
	
	protected abstract void excelFinsihCall();
	
	@SuppressWarnings("unchecked")
	protected final <T extends DataSupplier> T getDataSupplier(Class<T> clz) {
		return (T) dataSupliers.get(clz);
	}

	@Override
	public final <T extends DataSupplier> void addDataSuplier(Class<T> clz, DataSupplier suplier) {
		dataSupliers.put(clz, suplier);
	}

}
