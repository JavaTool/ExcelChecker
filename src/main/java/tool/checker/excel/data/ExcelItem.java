package tool.checker.excel.data;

import java.util.Map;

import com.google.common.collect.Maps;

public class ExcelItem {
	
	private String name;
	
	private String type;
	
	private int colum;
	
	private Map<String, String> attributes = Maps.newHashMap();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColum() {
		return colum;
	}

	public void setColum(int colum) {
		this.colum = colum;
	}
	
	public void putAttribute(String key, String attribute) {
		attributes.put(key, attribute);
	}
	
	public String getAttribute(String key) {
		return attributes.get(key);
	}

}
