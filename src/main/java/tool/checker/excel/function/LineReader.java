package tool.checker.excel.function;

/**
 * 行读取器
 * @author fuhuiyuan
 * @since 1.0.1
 */
public interface LineReader {
	
	/**
	 * 读取一行
	 * @param 	lineNumber
	 * 			行号
	 * @param 	line
	 * 			行内容
	 */
	void readLine(int lineNumber, String line);

}
