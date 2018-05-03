package tool.checker.excel.config;

import java.util.Map;

/**
 * 默认配置加载组件，按行读取相应配置内容。
 * @author fuhuiyuan
 * @since 1.0.0
 */
public final class DefaultConfigLoader extends BaseConfigLoader {
	
	/** 配置键数组 */
	private static final String[] KEYS = {KEY_ALL_PATH, KEY_IGNORE, KEY_PATHS, KEY_RELATION_PATH, KEY_OUT_PATH};

	@Override
	protected void readConfigs(Map<String, String> map, int lineNumber, String line) {
		if (line.trim().length() > 0) {
			map.put(KEYS[lineNumber], line);
		}
	}

}
