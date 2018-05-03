package tool.checker.excel.data;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Excel列信息
 * @author fuhuiyuan
 * @since 1.0.0
 */
public class ExcelItem {
	
	/** 列名称 */
	private String name;
	/** 数据类型 */
	private String type;
	/** 列号 */
	private int colum;
	/** 列属性集合[key=属性名称，value=属性值] */
	private Map<String, String> attributes = Maps.newHashMap();

	/**
	 * 获取数据类型
	 * @return	数据类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置数据类型
	 * @param 	type
	 * 			数据类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取列名称
	 * @return	列名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置列名称
	 * @param 	name
	 * 			列名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取列号
	 * @return	列号
	 */
	public int getColum() {
		return colum;
	}

	/**
	 * 设置列号
	 * @param 	colum
	 * 			列号
	 */
	public void setColum(int colum) {
		this.colum = colum;
	}
	
	/**
	 * 添加列属性
	 * @param 	key
	 * 			属性名称
	 * @param 	attribute
	 * 			属性值
	 */
	public void putAttribute(String key, String attribute) {
		attributes.put(key, attribute);
	}
	
	/**
	 * 获取列属性
	 * @param 	key
	 * 			属性名称
	 * @return	属性值
	 */
	public String getAttribute(String key) {
		return attributes.get(key);
	}

}
