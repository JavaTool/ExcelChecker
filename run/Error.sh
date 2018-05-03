basepath=$(cd `dirname $0`; pwd)
java -cp ".;lib/*" tool.checker.excel.ExcelChecker basepath