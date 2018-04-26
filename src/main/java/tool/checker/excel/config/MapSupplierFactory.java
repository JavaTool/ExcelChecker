package tool.checker.excel.config;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import tool.checker.excel.Excel;
import tool.checker.excel.Utils;

public class MapSupplierFactory implements SupplierFactory {

	@Override
	public Function<String, Excel> createSupplier() {
		return new Function<String, Excel>() {
			
			private Map<String, Excel> excels = Maps.newHashMap();

			@Override
			public Excel apply(String input) {
				input = Utils.getFileName(input);
				Excel excel = excels.get(input);
				if (excel == null) {
					excel = new Excel();
				}
				excels.put(input, excel);
				return excel;
			}
			
		};
	}

}
