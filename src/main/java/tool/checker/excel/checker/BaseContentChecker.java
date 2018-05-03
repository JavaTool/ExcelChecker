package tool.checker.excel.checker;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.function.DataSupplier;

/**
 * 内容检测组件基类，实现部分公共属性。
 * @author fuhuiyuan
 * @since 1.0.0
 */
public abstract class BaseContentChecker implements ContentChecker {
	
	/** 当前检测的Excel数据 */
	protected BaseExcel excel;
	/** 当前检测的行号 */
	protected int row;
	/** 数据提供组件集合，需要使用类注解来指定需要的组件。 */
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
	
	/**
	 * Excel检测开始的处理过程
	 */
	protected abstract void excelBegin();
	
	/**
	 * 行检测开始的处理过程
	 */
	protected abstract void rowBegin();
	
	/**
	 * Excel检测结束的处理过程
	 */
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
