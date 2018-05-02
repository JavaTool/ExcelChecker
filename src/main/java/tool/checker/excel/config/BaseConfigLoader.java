package tool.checker.excel.config;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import tool.checker.excel.Utils;
import tool.checker.excel.data.ExcelsData;
import tool.checker.excel.function.LineReader;

public abstract class BaseConfigLoader implements ConfigLoader {

	@Override
	public void load(ExcelsData excelsData) {
		final Map<String, String> map = Maps.newHashMap();
		map.put(KEY_ALL_PATH, VALUE_ALL_PATH);
		map.put(KEY_IGNORE, VALUE_IGNORE);
		map.put(KEY_PATHS, VALUE_PATHS);
		map.put(KEY_RELATION_PATH, VALUE_RELATION_PATH);
		map.put(KEY_OUT_PATH, VALUE_OUT_PATH);
		
		Utils.readLines(new File(excelsData.getDir(), "path.txt"), new LineReader() {
			
			@Override
			public void readLine(int lineNumber, String line) {
				readConfigs(map, lineNumber, line);
			}
			
		});
		
		excelsData.setConfigs(new Function<String, String>() {

			@Override
			public String apply(String input) {
				return map.get(input);
			}
			
		});
	}
	
	protected abstract void readConfigs(Map<String, String> map, int lineNumber, String line);

}
