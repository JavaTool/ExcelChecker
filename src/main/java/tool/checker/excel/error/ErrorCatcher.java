package tool.checker.excel.error;

import java.util.List;

public interface ErrorCatcher {
	
	void catchError(String excelName, String error);
	
	void catchError(String excelName, int row, String columnName, String error);
	
	List<String> getErrors();

}
