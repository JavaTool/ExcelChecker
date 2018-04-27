package tool.checker.excel;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;

import com.google.common.collect.Lists;

import tool.checker.excel.checker.ContentChecker;
import tool.checker.excel.config.ConfigsFactory;
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
			ConfigsFactory configsFactory = (ConfigsFactory) Class.forName(reader.readLine()).newInstance();
			excelsData.setConfigs(configsFactory.createConfigs(readLines(new File(dir, "path.txt")).toArray(new String[0])));
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
	
	private static List<String> readLines(File file) {
		List<String> lines = Lists.newLinkedList();
		try (LineNumberReader pathReader = new LineNumberReader(new FileReader(file))) {
			String line;
			while ((line = pathReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return lines;
	}

}
