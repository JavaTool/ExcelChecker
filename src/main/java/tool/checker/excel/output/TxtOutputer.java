package tool.checker.excel.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tool.checker.excel.data.ExcelsData;

/**
 * 记事本输出组件
 * @author fuhuiyuan
 * @since 1.0.0
 */
public class TxtOutputer implements Outputer {

	@Override
	public void out(ExcelsData excelsData) {
		// 创建文件
		File file = createNewFile(excelsData);
		// 组织输出
		StringBuilder builder = new StringBuilder();
		for (String error : excelsData.getErrorCatcher().getErrors()) {
			builder.append(error).append("\r\n");
		}
		// 输出到文件
		try (FileWriter writer = new FileWriter(file)) {
			writer.append(builder.length() == 0 ? "没错，真棒！！" : builder.toString());
			writer.flush();
			System.out.println("Generate " + file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建文件
	 * @param 	excelsData
	 * 			Excel整体数据
	 */
	private File createNewFile(ExcelsData excelsData) {
		File file = new File(excelsData.getDir(), excelsData.getConfigs().apply("outPath"));
		file.delete();
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

}
