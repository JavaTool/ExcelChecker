# ExcelChecker
For check excel correctness and relations.<br>
excelStruct -> config -> create error catcher -> custom find & load -> custom check -> output

<a href="https://github.com/JavaTool/ExcelChecker/wiki"><h1>已发布版本下载信息</h1></a>

<b>一、环境要求：</b>
<ol>
<li>JDK1.7+。</li>
<li><a href="https://jingyan.baidu.com/article/fd8044fa2c22f15031137a2a.html">配置java环境变量。</a></li>
</ol>

<b>二、使用说明：</b>
<ol>
<li>config文件没有特殊说明，请不要修改。</li>
<li>Error.bat为执行文件，请勿修改。</li>
<li>错误信息会输出到excel_check_result.txt(可配置在path文件，见三.4)中。</li>
<li>基础检测为数据检测所需的加载策略是否存在。</li>
</ol>

<b>三、path文件说明：</b>
<ol>
<li>第一行为Excel根目录。</li>
<li>(可无)第二行为忽略的Excel，用半角逗号间隔(比如Equipment.xls、Equipment.xls,Item.xlsx)，不填表示无忽略。</li>
<li>(可无)第三行为检测的Excel路径，用半角逗号间隔(比如equip、equip/Equipment.xls、equip,Bag、equip/Equipment.xls,Bag)，不填表示全检测。</li>
<li>(可无)第四行为Excel关系表名称，需要在本路径内，不填名称为ExcelCheckerConfig.xlsx。</li>
<li>(可无)第五行为错误信息文件名称，需要在本路径内，不填名称为excel_check_result.txt。</li>
</ol>

<b>四、Excel关系表说明：</b>
<ol>
<li>每张Sheet的内容需要按照所选加载策略来填写。</li>
</ol>

<b>五、错误收集 & 信息输出 策略：</b>
<ol>
<li>tool.checker.excel.error.ExcelErrors：错误信息分为四项：表名、行号、列名和错误信息，排版方式为空格填充表名、行、列，并右对齐。</li>
<li>tool.checker.excel.output.TxtOutputer：将错误信息按行输出，错误文件会删除已存在的文件，无措时有提示内容。</li>
</ol>

<b>六、config文件说明(从上到下表示了处理流程，示例见文件)：</b>
<ol>
<li>第一行：Excel数据结构(用于匹配不同的表定义方式)。</li>
<li>第二行：Config表加载策略。</li>
<li>第三行：错误记录策略。</li>
<li>第四行：加载策略组，需要半角逗号分割的完整类名。</li>
<li>第五行：检测策略组，需要半角逗号分割的完整类名。</li>
<li>第六行：错误输出策略。</li>
</ol>

<b>七、加载策略：</b>
<ol>
<li>tool.checker.excel.finder.DefaultExcelFinder：记录全部文件，按指定Excel的数据结构加载Excel数据。</li>
<li>tool.checker.excel.finder.RelationExcelFinder：加载引用关系，Excel关系表第一张表为关系表，用来检测引用是否存在。第一行表头不读取，需要四列：表名(被检测表)	列名(被检测列)	外联表(引用的表，可以是被检测的表)	外联列(引用的列)。</li>
<li>tool.checker.excel.finder.ExcelArrayFinder：加载相同数组长度的列信息，Excel关系表arrayGroups表为同长度数组列表，用来检测几个数组列的数组长度是否相同，列名之间用半角逗号分割(例如composeItems,nums)。</li>
</ol>

<b>八、检测策略：</b>
<ol>
<li>tool.checker.excel.checker.YunChangPrimaryChecker：检测YunChangExcel规范的主键唯一性。</li>
<li>tool.checker.excel.checker.YunChangDataTypeChecker：检测YunChangExcel规范的数据类型正确性。</li>
<li>tool.checker.excel.checker.RelationChecker：检测加载策略1(tool.checker.excel.finder.RelationExcelFinder)的引用正确性。</li>
<li>tool.checker.excel.checker.YunChangArrayChecker：检测加载策略2(tool.checker.excel.finder.ExcelArrayFinder)记录的各列数组长度是否相同。</li>
</ol>

<b>YunChangExcel规范(tool.checker.excel.data.YunChangExcel实现方式)：</b>
<ol>
<li>第一行：A格为数据起始行号，B格为数据起始列号，C格为数据结束行号(如果读取时发现整行为空，则提前结束)，D格为数据结束列号。</li>
<li>第二行：说明。</li>
<li>第三行：客户端变量名称(不填表示不用)。</li>
<li>第四行：数据类型(int、string、double、float、array_int、array_string、array_double、array_float，array开头的类型服务器都算string)。</li>
<li>第五行：索引名称(none为无，primary为主键)。</li>
<li>第六行：服务器变量名称(不填表示不用)。</li>
<li>不填的格子，数值类型默认转为数值0。</li>
<li>array类型需要用&&分割。</li>
<li>Excel关系表第一张表为关系表，用来检测引用是否存在。第一行表头不读取，需要四列：表名(被检测表)	列名(被检测列)	外联表(引用的表，可以是被检测的表)	外联列(引用的列)。</li>
<li>Excel关系表arrayGroups表为同长度数组列表，用来检测几个数组列的数组长度是否相同，列名之间用半角逗号分割(例如composeItems,nums)。</li>
</ol>
