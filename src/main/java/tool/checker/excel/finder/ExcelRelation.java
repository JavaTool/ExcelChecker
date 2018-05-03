package tool.checker.excel.finder;

/**
 * Excel关系
 * @author fuhuiyuan
 * @since 1.0.0
 */
public class ExcelRelation {
	
	/** 外联列名称 */
	private final String foreign;
	/** 引用的表名称 */
	private final String excel;
	
	public ExcelRelation(String excel, String foreign) {
		this.excel = excel;
		this.foreign = foreign;
	}

	/**
	 * 获取引用的表名称
	 * @return	引用的表名称
	 */
	public String getExcel() {
		return excel;
	}

	/**
	 * 获取外联列名称
	 * @return	外联列名称
	 */
	public String getForeign() {
		return foreign;
	}

}
