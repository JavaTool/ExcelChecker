package tool.checker.excel.finder;

/**
 * 枚举和引用关系数据提供组件
 * @author fuhuiyuan
 * @since 2.0.0
 */
public interface EnumAndRelationSupplier extends RelationSupplier {
	
	/**
	 * 判断是否包含指定枚举的内容
	 * @param 	type
	 * 			枚举
	 * @param 	content
	 * 			内容
	 * @return	是否包含指定枚举的内容
	 */
	boolean containsEnum(String type, String content);

}
