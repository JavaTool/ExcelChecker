package tool.checker.excel.finder;

public interface EnumAndRelationSupplier extends RelationSupplier {
	
	boolean containsEnum(String type, String content);

}
