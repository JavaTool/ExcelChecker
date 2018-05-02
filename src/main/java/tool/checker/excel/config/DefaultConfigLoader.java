package tool.checker.excel.config;

import java.util.Map;

public final class DefaultConfigLoader extends BaseConfigLoader {
	
	private static final String[] KEYS = {KEY_ALL_PATH, KEY_IGNORE, KEY_PATHS, KEY_RELATION_PATH, KEY_OUT_PATH};

	@Override
	protected void readConfigs(Map<String, String> map, int lineNumber, String line) {
		if (line.trim().length() > 0) {
			map.put(KEYS[lineNumber], line);
		}
	}

}
