package tool.checker.excel.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import com.google.common.base.Function;

public final class TxtOutputerFactory implements OutputerFactory {

	@Override
	public Outputer createOutputer() {
		return new Outputer() {
			
			@Override
			public void out(File dir, Function<String, String> configs, Collection<String> errors) {
				File file = new File(dir, configs.apply("outPath"));
				file.delete();
				file.getParentFile().mkdirs();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				StringBuilder builder = new StringBuilder();
				for (String error : errors) {
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
			
		};
	}

}
