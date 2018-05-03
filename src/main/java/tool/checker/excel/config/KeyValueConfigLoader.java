package tool.checker.excel.config;

import java.util.Map;

import tool.checker.excel.Utils;

/**
 * 键值对配置加载组件，按键值对(键=值)读取相应配置内容。
 * @author fuhuiyuan
 * @since 1.0.1
 */
public final class KeyValueConfigLoader extends BaseConfigLoader {

	@Override
	protected void readConfigs(Map<String, String> map, int lineNumber, String line) {
		Utils.saveKeyValue(line, map);
		System.out.println(line);
	}

}
