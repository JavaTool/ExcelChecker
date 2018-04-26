package tool.checker.excel.finder;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import tool.checker.excel.Utils;

public final class DefaultLoadFinderFactory implements LoadFinderFactory {

	@Override
	public Function<Function<String, String>, Map<String, File>> createLoadFinder() {
		return new Function<Function<String,String>, Map<String,File>>() {
			
			@Override
			public Map<String, File> apply(Function<String, String> input) {
				final Map<String, File> loadFiles = Maps.newHashMap();
				String allPath = input.apply("allPath");
				String config = input.apply("paths");
				for (String path : Strings.isNullOrEmpty(config) ? new String[]{""} : config.split(",")) {
					findLoadFiles(loadFiles, new File(allPath + "\\" + path));
				}
				return loadFiles;
			}
			
		};
	}
	
	private static void findLoadFiles(Map<String, File> files, File file) {
		if (file.isFile()) {
			String fileName = file.getName();
			if (Utils.isExcelFile(fileName)) {
				files.put(Utils.getFileName(fileName), file);
				System.out.println("加载需要检测的文件[" + fileName + "]");
			} else {
				System.out.println("忽略文件[" + fileName + "]");
			}
		} else {
			for (File subFile : file.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					return pathname.isDirectory() || Utils.isExcelFile(pathname.getName());
				}
				
			})) {
				findLoadFiles(files, subFile);
			}
		}
	}

}
