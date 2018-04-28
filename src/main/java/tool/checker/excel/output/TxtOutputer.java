package tool.checker.excel.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tool.checker.excel.data.ExcelsData;

public class TxtOutputer implements Outputer {

	@Override
	public void out(ExcelsData excelsData) {
		File file = new File(excelsData.getDir(), excelsData.getConfigs().apply("outPath"));
		file.delete();
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		for (String error : excelsData.getErrorCatcher().getErrors()) {
			builder.append(error).append("\r\n");
		}
		try (FileWriter writer = new FileWriter(file)) {
			writer.append(builder.length() == 0 ? "没错，真棒！！" : builder.toString());
			writer.flush();
			System.out.println("Generate " + file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
