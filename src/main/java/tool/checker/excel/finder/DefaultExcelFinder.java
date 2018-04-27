package tool.checker.excel.finder;

import java.io.File;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Strings;

import tool.checker.excel.Excel;
import tool.checker.excel.ExcelsData;
import tool.checker.excel.Utils;
import tool.checker.excel.function.Callback;
import tool.checker.excel.function.ExcelFilter;

public final class DefaultExcelFinder implements ExcelFinder {

	@Override
	public void find(final ExcelsData excelsData) {
		Function<String, String> configs = excelsData.getConfigs();
		String allPath = configs.apply("allPath");
		String config = configs.apply("paths");
		File parent = new File(allPath);
		final Map<String, File> files = excelsData.getFiles();
		// 记录全部文件
		Utils.findLoadFiles(parent, ExcelFilter.EXCEL_FILTER, new Callback<File>() {
			
			@Override
			public void callback(File t) {
				files.put(Utils.getFileName(t.getName()), t);
			}
			
		});
		
		final Map<String, Excel> excels = excelsData.getExcels();
		Callback<File> callback = new Callback<File>() {

			@Override
			public void callback(File t) {
				String fileName = t.getName();
				if (Utils.isExcelFile(fileName)) {
					String key = Utils.getFileName(fileName);
					excels.put(key, Utils.createExcel(t, excelsData.getErrorCatcher()));
					files.remove(key);
					System.out.println("加载需要检测的文件[" + fileName + "]");
				} else {
					System.out.println("忽略文件[" + fileName + "]");
				}
			}
			
		};
		// 加载需要检测的Excel
		for (String path : Strings.isNullOrEmpty(config) ? new String[]{""} : config.split(",")) {
			Utils.findLoadFiles(new File(parent, path), ExcelFilter.EXCEL_FILTER, callback);
		}
	}

}
