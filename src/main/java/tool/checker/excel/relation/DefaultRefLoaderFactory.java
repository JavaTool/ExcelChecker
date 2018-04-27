package tool.checker.excel.relation;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Function;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.ErrorCatcher;
import tool.checker.excel.Excel;
import tool.checker.excel.Utils;

public final class DefaultRefLoaderFactory implements RefLoaderFactory {

	@Override
	public RefLoader createRefLoader() {
		return new RefLoader() {
			
			@Override
			public void loadRefs(	Function<String, String> configs, final Map<String, File> loadFiles,
									final Function<String, Excel> supplier, SetMultimap<String, String> refKeys, ErrorCatcher errorCatcher) {
				loadRef(new File(configs.apply("allPath")), new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						return pathname.isDirectory() || loadFiles.containsKey(Utils.getFileName(pathname.getName()));
					}
					
				}, new Function<String, Excel>() {

					@Override
					public Excel apply(String input) {
						return loadFiles.containsKey(Utils.getFileName(input)) ? supplier.apply(input) : null;
					}
					
				}, refKeys, errorCatcher);
			}
			
		};
	}
	
	private static void loadRef(File file, FileFilter filter, Function<String, Excel> supplier, SetMultimap<String, String> refKeys, ErrorCatcher errorCatcher) {
		if (file.isFile()) {
			String fileName = file.getName();
			String excelName = Utils.getFileName(fileName);
			if (Utils.isExcelFile(fileName) && refKeys.containsKey(excelName)) {
				try {
					Workbook wb = Utils.createWorkbook(file);
					Excel excel = supplier.apply(excelName);
					Sheet sheet = wb.getSheetAt(0);
					excel.setExcelName(fileName);
//					excel.loadExcel(sheet, refKeys.get(excelName), errorCatcher);
//					if (!excel.isLoad()) {
//						return;
//					}
//					excel.loadRefs(sheet);
					System.out.println("加载引用表 : " + fileName);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("加载引用表 : " + fileName + "错误");
				}
			} else {
				System.out.println("忽略文件[" + fileName + "]");
			}
		} else {
			for (File subFile : file.listFiles(filter)) {
				loadRef(subFile, filter, supplier, refKeys, errorCatcher);
			}
		}
	}

}
