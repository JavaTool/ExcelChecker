package tool.checker.excel.error;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class ExcelErrors implements ErrorCatcher {
	
	private List<String> errors = Lists.newLinkedList();

	@Override
	public void catchError(String excelName, int row, String columnName, String error) {
		StringBuilder builder = new StringBuilder();
		builder.append("表[").append(Strings.padStart(excelName, 30, ' ')).append("] ");
		builder.append("行[").append(Strings.padStart(row + "", 4, ' ')).append("] ");
		builder.append("列[").append(Strings.padStart(columnName + "", 10, ' ')).append("] ");
		builder.append("错[").append(error).append("] ");
		catchError(builder.toString());
	}

	@Override
	public List<String> getErrors() {
		return ImmutableList.copyOf(errors);
	}

	@Override
	public void catchError(String excelName, String error) {
		StringBuilder builder = new StringBuilder();
		builder.append("表[").append(Strings.padStart(excelName, 30, ' ')).append("] ");
		builder.append("错[").append(error).append("] ");
		catchError(builder.toString());
	}

	@Override
	public void catchError(String error) {
		errors.add(error);
	}

}
