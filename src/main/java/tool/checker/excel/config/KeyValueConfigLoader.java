package tool.checker.excel.config;

import java.util.Map;

import tool.checker.excel.Utils;

public final class KeyValueConfigLoader extends BaseConfigLoader {

	@Override
	protected void readConfigs(Map<String, String> map, int lineNumber, String line) {
		Utils.saveKeyValue(line, map);
		System.out.println(line);
	}

}
