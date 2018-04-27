package tool.checker.excel.function;

import java.util.List;

public interface ErrorCatcher {
	
	void catchError(String excelName, int row, String columnName, String error);
	
	List<String> getErrors();

}
