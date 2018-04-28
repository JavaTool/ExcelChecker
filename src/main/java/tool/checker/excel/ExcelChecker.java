package tool.checker.excel;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.config.ConfigLoader;
import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelsData;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.ExcelFinder;
import tool.checker.excel.output.Outputer;

final class ExcelChecker {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String configPath = args.length > 0 ? args[0].replace("\\", "\\\\") : "F:\\software\\product\\excelChecker\\";
		File dir = new File(configPath);
		ExcelsData excelsData = new ExcelsData();
		excelsData.setDir(dir);
		try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(dir, "config.txt")))) {
			// 加载Excel类
			excelsData.setExcelClass((Class<BaseExcel>) Class.forName(reader.readLine()));
			// 加载配置
			((ConfigLoader) Class.forName(reader.readLine()).newInstance()).load(excelsData);
			// 创建错误收集策略
			excelsData.setErrorCatcher((ErrorCatcher) Class.forName(reader.readLine()).newInstance());
			// 执行加载策略组
			for (String finderName : reader.readLine().split(",")) {
				((ExcelFinder) Class.forName(finderName).newInstance()).find(excelsData);
			}
			// 创建检测策略组
			for (String checkerName : reader.readLine().split(",")) {
				ContentChecker checker = ((ContentChecker) Class.forName(checkerName).newInstance());
				for (BaseExcel excel : excelsData.getExcels().values()) {
					excel.addChecker(checker);
				}
			}
			// 检测
			for (BaseExcel excel : excelsData.getExcels().values()) {
				excel.checkData(excelsData);
			}
			// 创建输出策略
			((Outputer) Class.forName(reader.readLine()).newInstance()).out(excelsData);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
