package tool.checker.excel;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;

import tool.checker.excel.checker.CheckerFactory;
import tool.checker.excel.config.ConfigsFactory;
import tool.checker.excel.config.SupplierFactory;
import tool.checker.excel.finder.LoadFinderFactory;
import tool.checker.excel.output.OutputerFactory;
import tool.checker.excel.relation.RefLoaderFactory;
import tool.checker.excel.relation.RelationLoaderFactory;

public class ExcelChecker {
	
	public static void main(String[] args) {
		String configPath = args.length > 0 ? args[0].replace("\\", "\\\\") : "F:\\software\\product\\excelChecker\\";
		File dir = new File(configPath);
		try (LineNumberReader reader = new LineNumberReader(new FileReader(new File(dir, "config.txt")))) {
			ConfigsFactory configsFactory = (ConfigsFactory) Class.forName(reader.readLine()).newInstance();
			LoadFinderFactory loadFinderFactory = (LoadFinderFactory) Class.forName(reader.readLine()).newInstance();
			SupplierFactory supplierFactory = (SupplierFactory) Class.forName(reader.readLine()).newInstance();
			RelationLoaderFactory relationLoaderFactory = (RelationLoaderFactory) Class.forName(reader.readLine()).newInstance();
			RefLoaderFactory refLoaderFactory = (RefLoaderFactory) Class.forName(reader.readLine()).newInstance();
			CheckerFactory checkerFactory = (CheckerFactory) Class.forName(reader.readLine()).newInstance();
			OutputerFactory outputerFactory = (OutputerFactory) Class.forName(reader.readLine()).newInstance();
			ErrorCatcher errors = (ErrorCatcher) Class.forName(reader.readLine()).newInstance();

			Function<String, String> configs = configsFactory.createConfigs(readLines(new File(dir, "path.txt")).toArray(new String[0]));
			Map<String, File> loadFiles = loadFinderFactory.createLoadFinder().apply(configs);
			Function<String, Excel> supplier = supplierFactory.createSupplier();
			SetMultimap<String, String> refKeys = relationLoaderFactory.createRelationLoader().loadConfig(dir, configs, supplier);
			refLoaderFactory.createRefLoader().loadRefs(configs, loadFiles, supplier, refKeys, errors);
			checkerFactory.createChecker().check(loadFiles, supplier, errors);
			outputerFactory.createOutputer().out(dir, configs, errors.getErrors());
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
