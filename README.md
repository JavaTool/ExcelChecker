# ExcelChecker
For check excel correctness and relations.<br>
config -> create error catcher -> find & load -> custom find & load -> custom check -> output

<a href="https://github.com/JavaTool/ExcelChecker/wiki"><h1>已发布版本下载信息</h1></a>

<b>一、环境要求：</b>
<ol>
<li>JDK1.7+。</li>
<li><a href="https://jingyan.baidu.com/article/fd8044fa2c22f15031137a2a.html">配置java环境变量。</a></li>
</ol>

<b>二、使用说明：</b>
<ol>
<li>config文件没有特殊说明，请不要修改。</li>
<li>Error.bat为执行文件，首次执行或本软件文件夹位置发生变化时需要修改内容。右键该文件选择编辑，将第一行最后一段双引号中的内容替换为当前路径。</li>
<li>错误信息会输出到excel_check_result.txt中。</li>
</ol>

<b>三、path文件说明：</b>
<ol>
<li>第一行为Excel根目录。</li>
<li>第二行为检测的Excel路径，用半角逗号间隔(比如equip、equip/Equipment.xls、equip,Bag、equip/Equipment.xls,Bag)，不填表示全检测。</li>
<li>(可无)第三行为Excel关系表名称，需要在本路径内。</li>
<li>(可无)第四行为Excel关系表名称，需要在本路径内。</li>
</ol>

<b>四、Excel关系表说明：</b>
<ol>
<li>第一张表为关系表，用来检测引用是否存在。第一行表头不读取，需要四列：表名(被检测表)	列名(被检测列)	外联表(引用的表，可以是被检测的表)	外联列(引用的列)。</li>
<li>第二张表为同长度数组列表，用来检测几个数组列的数组长度是否相同，列名之间用半角逗号分割(例如composeItems,nums)。</li>
</ol>

<b>五、错误信息说明：</b>
<ol>
<li>错误信息分为四项：表名、行号、列名和错误信息。</li>
<li>排版方式为空格填充表名、行、列，并右对齐。</li>
</ol>

<b>六、config文件说明(从上到下表示了处理流程，示例见文件)：</b>
<ol>
<li>第一行：Config表加载策略。</li>
<li>第二行：错误记录策略。</li>
<li>第三行：加载策略组，需要半角逗号分割的完整类名。</li>
<li>第四行：检测策略组，需要半角逗号分割的完整类名。</li>
<li>第五行：错误输出策略略。</li>
</ol>

<b>七、被检测Excel规范：</b>
<ol>
<li>第一行：A格为数据起始行号，B格为数据起始列号，C格为数据结束行号(如果读取时发现整行为空，则提前结束)，D格为数据结束列号。</li>
<li>第二行：说明。</li>
<li>第三行：客户端变量名称(不填表示不用)。</li>
<li>第四行：数据类型(int、string、double、float、array_int、array_string、array_double、array_float，array开头的类型服务器都算string)。</li>
<li>第五行：索引名称(none为无，primary为主键)。</li>
<li>第六行：服务器变量名称(不填表示不用)。</li>
<li>不填的格子，数值类型默认转为数值0。</li>
<li>array类型需要用&&分割。</li>
</ol>
