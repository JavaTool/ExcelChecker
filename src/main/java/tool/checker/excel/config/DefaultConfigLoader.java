package tool.checker.excel.config;

import java.util.List;
import java.util.Map;

public final class DefaultConfigLoader extends BaseConfigLoader {

	@Override
	protected void readConfigs(Map<String, String> map, List<String> lines) {
		readLine(map, KEY_ALL_PATH, lines, 0);
		readLine(map, KEY_IGNORE, lines, 1);
		readLine(map, KEY_PATHS, lines, 2);
		readLine(map, KEY_RELATION_PATH, lines, 3);
		readLine(map, KEY_OUT_PATH, lines, 4);
	}
	
	private void readLine(Map<String, String> map, String key, List<String> lines, int index) {
		if (lines.size() > index && lines.get(index).trim().length() > 0) {
			map.put(key, lines.get(index));
		}
	}

}
