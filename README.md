# ExcelChecker
For check excel correctness and relations.

<b>一、配置要求：</b>
<ol>
<li>JDK1.7+。</li>
<li><a href="https://jingyan.baidu.com/article/fd8044fa2c22f15031137a2a.html">配置java环境变量。</a></li>
</ol>

<b>二、使用说明：</b>
<ol>
<li>config文件没有特殊说明，请不要修改。</li>
<li>Error.bat为执行文件，首次执行或本软件文件夹位置发生变化时需要修改内容。右键该文件选择编辑，将第一行最后一段双引号中的内容替换为当前路径。</li>
<li>path.txt说明：第一行为Excel关系表名称，需要在本路径内；第二行为Excel目录；第三行为检测的Excel路径，用半角逗号间隔(比如equip、equip/Equipment.xls、equip,Bag、equip/Equipment.xls,Bag)，不填表示全检测；第四行为输出错内容文件名，需要在本路径内。</li>
</ol>

<b>三、Excel关系表说明：</b>
<ol>
<li>第一张表为关系表，用来检测引用是否存在，需要四列：表名(被检测表)	列名(被检测列)	外联表(引用的表，可以是被检测的表)	外联列(引用的列)。</li>
</ol>
