package tool.checker.excel.config;

import com.google.common.base.Function;

public interface ConfigsFactory {
	
	Function<String, String> createConfigs(String[] args);

}
