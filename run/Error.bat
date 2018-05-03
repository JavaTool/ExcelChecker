set local=%~dp0
java -cp ".;lib/*" tool.checker.excel.ExcelChecker "%local:~0,-1%"
pause