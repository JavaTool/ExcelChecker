package tool.checker.excel.function;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据消费组件注解
 * @author fuhuiyuan
 * @since 1.0.0
 */
@Target(ElementType.TYPE) // ElementType.TYPE用于标识类、接口(包括内注自身)、枚举  
@Retention(RetentionPolicy.RUNTIME) // 存在RetentionPolicy.RUNTIME、RetentionPolicy.CLASS、RetentionPolicy.SOURCE
@Documented // javadoc可生成相应文档
@Inherited // 可继承
public @interface DataConsumer {
	
	/**
	 * @return	类型数组
	 */
	Type[] value();
	
	/**
	 * 类型
	 * @author fuhuiyuan
	 * @since 1.0.0
	 */
	public @interface Type {
		
		/**
		 * @return	所需的数据提供组件类型
		 */
		public Class<? extends DataSupplier> value();
		
	}

}
