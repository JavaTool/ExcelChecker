package tool.checker.excel;

import java.io.File;
import java.util.Map;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.config.ConfigLoader;
import tool.checker.excel.data.BaseExcel;
import tool.checker.excel.data.ExcelsData;
import tool.checker.excel.error.ErrorCatcher;
import tool.checker.excel.finder.ExcelFinder;
import tool.checker.excel.function.DataConsumer;
import tool.checker.excel.output.Outputer;

/**
 * 主类
 * @author fuhuiyuan
 * @since 1.0.0
 */
final class ExcelChecker {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String configPath = args.length > 0 ? args[0].replace("\\", "\\\\") : "config";
		File dir = new File(configPath);
		ExcelsData excelsData = new ExcelsData();
		excelsData.setDir(dir);
		Map<String, String> configs = Utils.readLines(new File(dir, "config.txt"));
		try {
			// 加载Excel类
			excelsData.setExcelClass((Class<BaseExcel>) Class.forName(configs.get("ExcelStruct")));
			// 加载配置
			((ConfigLoader) Class.forName(configs.get("ConfigLoader")).newInstance()).load(excelsData);
			// 创建错误收集策略
			excelsData.setErrorCatcher((ErrorCatcher) Class.forName(configs.get("ErrorCatcher")).newInstance());
			// 执行加载策略组
			for (String finderName : configs.get("ExcelFinder").split(",")) {
				((ExcelFinder) Class.forName(finderName).newInstance()).find(excelsData);
			}
			// 创建检测策略组
			createCheckers(configs.get("ExcelChecker").split(","), excelsData);
			// 检测
			for (BaseExcel excel : excelsData.getExcels().values()) {
				excel.checkData(excelsData);
			}
			// 创建输出策略
			((Outputer) Class.forName(configs.get("Outputer")).newInstance()).out(excelsData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建全部检测组件
	 * @param 	checkerNames
	 * 			组件名称数组
	 * @param 	excelsData
	 * 			Excel整体数据
	 * @throws 	Exception
	 */
	private static void createCheckers(String[] checkerNames, ExcelsData excelsData) throws Exception {
		for (String checkerName : checkerNames) {
			@SuppressWarnings("unchecked")
			Class<ContentChecker> clz = (Class<ContentChecker>) Class.forName(checkerName);
			ContentChecker checker = createChecker(clz, excelsData);
			if (checker == null) {
				continue;
			}
			for (BaseExcel excel : excelsData.getExcels().values()) {
				excel.addChecker(checker);
			}
		}
	}
	
	/**
	 * 创建检测组件
	 * @param 	clz
	 * 			组件类型
	 * @param 	excelsData
	 * 			Excel整体数据
	 * @return	检测组件
	 * @throws 	Exception
	 */
	private static ContentChecker createChecker(Class<ContentChecker> clz, ExcelsData excelsData) throws Exception {
		DataConsumer dataConsumer = clz.getAnnotation(DataConsumer.class);
		if (dataConsumer != null) {
			for (DataConsumer.Type type : dataConsumer.value()) {
				if (!excelsData.getDataSuppliers().containsKey(type.value())) {
					excelsData.getErrorCatcher().catchError("检测策略[" + clz.getName() + "]没有数据提供者[" + type.value().getName() + "].");
					return null;
				}
			}
		}
		ContentChecker checker = clz.newInstance();
		if (dataConsumer != null) {
			for (DataConsumer.Type type : dataConsumer.value()) {
				checker.addDataSuplier(type.value(), excelsData.getDataSuppliers().get(type.value()));
			}
		}
		return checker;
	}

}
