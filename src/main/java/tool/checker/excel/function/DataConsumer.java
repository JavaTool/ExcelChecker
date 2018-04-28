package tool.checker.excel.function;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // ElementType.TYPE用于标识类、接口(包括内注自身)、枚举  
@Retention(RetentionPolicy.RUNTIME) // 存在RetentionPolicy.RUNTIME、RetentionPolicy.CLASS、RetentionPolicy.SOURCE
@Documented // javadoc可生成相应文档
@Inherited // 可继承
public @interface DataConsumer {
	
	Type[] value();
	
	public @interface Type {
		
		public Class<? extends DataSupplier> value();
		
	}

}
