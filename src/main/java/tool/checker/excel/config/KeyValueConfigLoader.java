package tool.checker.excel.config;

import java.util.List;
import java.util.Map;

public final class KeyValueConfigLoader extends BaseConfigLoader {

	@Override
	protected void readConfigs(Map<String, String> map, List<String> lines) {
		for (String line : lines) {
			if (line.trim().length() > 0) {
				String[] infos = line.split("=", -2);
				map.put(infos[0].trim(), infos[1].trim());
				System.out.println(infos[0].trim() + " : " + infos[1].trim());
			}
		}
	}

}
