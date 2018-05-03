package tool.checker.excel.function;

/**
 * 通用回调函数
 * @author fuhuiyuan
 *
 * @param <T>
 */
public interface Callback<T> {
	
	/**
	 * 回调
	 * @param 	t
	 * 			回调参数
	 */
	void callback(T t);

}
