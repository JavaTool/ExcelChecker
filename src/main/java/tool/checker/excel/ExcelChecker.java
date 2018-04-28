package tool.checker.excel;

import java.io.File;
import java.util.List;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.config.ConfigLoader;
import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelsData;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.ExcelFinder;
import tool.checker.excel.function.DataConsumer;
import tool.checker.excel.output.Outputer;

final class ExcelChecker {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String configPath = args.length > 0 ? args[0].replace("\\", "\\\\") : "F:\\software\\product\\excelChecker\\";
		File dir = new File(configPath);
		ExcelsData excelsData = new ExcelsData();
		excelsData.setDir(dir);
		List<String> infoList = Utils.readLines(new File(dir, "config.txt"));
		try {
			// 加载Excel类
			excelsData.setExcelClass((Class<BaseExcel>) Class.forName(infoList.get(0)));
			// 加载配置
			((ConfigLoader) Class.forName(infoList.get(1)).newInstance()).load(excelsData);
			// 创建错误收集策略
			excelsData.setErrorCatcher((ErrorCatcher) Class.forName(infoList.get(2)).newInstance());
			// 执行加载策略组
			for (String finderName : infoList.get(3).split(",")) {
				((ExcelFinder) Class.forName(finderName).newInstance()).find(excelsData);
			}
			// 创建检测策略组
			for (String checkerName : infoList.get(4).split(",")) {
				Class<ContentChecker> clz = (Class<ContentChecker>) Class.forName(checkerName);
				String needClass = checkDataConsumer(clz.getAnnotation(DataConsumer.class), excelsData);
				if (needClass == null) {
					ContentChecker checker = (clz.newInstance());
					for (BaseExcel excel : excelsData.getExcels().values()) {
						excel.addChecker(checker);
					}
				} else {
					excelsData.getErrorCatcher().catchError("检测策略[" + checkerName + "]没有数据提供者[" + needClass + "].");
				}
			}
			// 检测
			for (BaseExcel excel : excelsData.getExcels().values()) {
				excel.checkData(excelsData);
			}
			// 创建输出策略
			((Outputer) Class.forName(infoList.get(5)).newInstance()).out(excelsData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String checkDataConsumer(DataConsumer dataConsumer, ExcelsData excelsData) {
		if (dataConsumer != null) {
			for (DataConsumer.Type type : dataConsumer.value()) {
				if (!excelsData.getClassToInstanceMap().containsKey(type.value())) {
					return type.value().getName();
				}
			}
		}
		return null;
	}

}
