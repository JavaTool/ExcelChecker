package tool.checker.excel.finder;

public class ExcelRelation {
	
	private final String foreign;
	
	private final String excel;
	
	public ExcelRelation(String excel, String foreign) {
		this.excel = excel;
		this.foreign = foreign;
	}

	public String getExcel() {
		return excel;
	}

	public String getForeign() {
		return foreign;
	}

}
