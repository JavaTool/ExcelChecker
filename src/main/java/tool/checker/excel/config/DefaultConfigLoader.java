package tool.checker.excel.config;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import tool.checker.excel.ExcelsData;
import tool.checker.excel.Utils;

public class DefaultConfigLoader implements ConfigLoader {

	@Override
	public void load(ExcelsData excelsData) {
		List<String> args = Utils.readLines(new File(excelsData.getDir(), "path.txt"));
		String allPath = args.size() > 0 ? args.get(0) : "D:\\workspace\\data\\excel";
		String paths = args.size() > 1 ? args.get(1) : "";
		String relationPath = args.size() > 2 && args.get(2).trim().length() > 0 ? args.get(2) : "ExcelCheckerConfig.xlsx";
		String outPath = args.size() > 3 && args.get(3).trim().length() > 0 ? args.get(3) : "excel_check_result.txt";
		System.out.println("外联关系配置文件路径 : " + relationPath);
		System.out.println("全部表路径 : " + allPath);
		System.out.println("检测文件组 : " + paths);
		System.out.println("错误文件输出路径 : " + outPath);
		
		final Map<String, String> map = Maps.newHashMap();
		map.put("relationPath", relationPath);
		map.put("allPath", allPath);
		map.put("paths", paths);
		map.put("outPath", outPath);
		
		excelsData.setConfigs(new Function<String, String>() {

			@Override
			public String apply(String input) {
				return map.get(input);
			}
			
		});
	}

}
