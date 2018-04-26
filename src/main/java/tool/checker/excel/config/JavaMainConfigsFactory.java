package tool.checker.excel.config;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public final class JavaMainConfigsFactory implements ConfigsFactory {

	@Override
	public Function<String, String> createConfigs(String[] args) {
		String relationPath = args.length > 0 ? args[0] : "D:\\ExcelCheckerConfig.xlsx";
		String allPath = args.length > 1 ? args[1] : "D:\\workspace\\data\\excel";
		String paths = args.length > 2 ? args[2] : "";
		String outPath = args.length > 3 ? args[3] : "D:\\excel_check_result.txt";
		System.out.println("外联关系配置文件路径 : " + relationPath);
		System.out.println("全部表路径 : " + allPath);
		System.out.println("检测文件组 : " + paths);
		System.out.println("错误文件输出路径 : " + outPath);
		
		final Map<String, String> map = Maps.newHashMap();
		map.put("relationPath", relationPath);
		map.put("allPath", allPath);
		map.put("paths", paths);
		map.put("outPath", outPath);
		return new Function<String, String>() {

			@Override
			public String apply(String input) {
				return map.get(input);
			}
			
		};
	}

}
