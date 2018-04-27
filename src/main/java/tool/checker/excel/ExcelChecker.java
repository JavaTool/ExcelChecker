package tool.checker.excel;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.config.ConfigLoader;
import tool.checker.excel.finder.ExcelFinder;
import tool.checker.excel.function.ErrorCatcher;
import tool.checker.excel.output.OutputerFactory;

public class ExcelChecker {
	
	public static void main(String[] args) {
		String configPath = args.length > 0 ? args[0].replace("\\", "\\\\") : "F:\\software\\product\\excelChecker\\";
		File dir = new File(configPath);
		ExcelsData excelsData = new ExcelsData();
		excelsData.setDir(dir);
		try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(dir, "config.txt")))) {
			ConfigLoader configLoader = (ConfigLoader) Class.forName(reader.readLine()).newInstance();
			configLoader.load(excelsData);
			excelsData.setErrorCatcher((ErrorCatcher) Class.forName(reader.readLine()).newInstance());
			for (String finderName : reader.readLine().split(",")) {
				((ExcelFinder) Class.forName(finderName).newInstance()).find(excelsData);
			}
			for (String checkerName : reader.readLine().split(",")) {
				ContentChecker checker = ((ContentChecker) Class.forName(checkerName).newInstance());
				for (Excel excel : excelsData.getExcels().values()) {
					excel.addChecker(checker);
				}
			}
			OutputerFactory outputerFactory = (OutputerFactory) Class.forName(reader.readLine()).newInstance();
			for (Excel excel : excelsData.getExcels().values()) {
				excel.checkData(excelsData);
			}
			outputerFactory.createOutputer().out(excelsData);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
