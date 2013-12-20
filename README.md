aoc
===

automatic orders checking framework
订单比对小框架。

我们经常需要处理外部订单对账文件。基于这些对账文件，与我们订单数据库中的订单进行对比，确认订单金额或者订单状态等。

## 文件检查校对功能
本小框架，首先提供一种简单的文件校对功能，包括：
+ 根据文件首行/末行中给出的共有多少行，共涉及多少金额来对整个对账文件进行校对；
+ 根据预先设置的规则，对每一行数据中的各个字段进行校验。

对账文件校对功能，提供一种简单的`输入(Input)`、`处理过滤(Filter)`、`输出(Output)`模型，使用properties属性描述，示例如下：

```ini
alias.Str=org.n3r.aoc.file.impl.validators.StrValidator
alias.Int=org.n3r.aoc.file.impl.validators.IntValidator
alias.Date=org.n3r.aoc.file.impl.validators.DateValidator
alias.Enum=org.n3r.aoc.file.impl.validators.EnumValidator
alias.Required=org.n3r.aoc.file.impl.validators.RequiredValidator
alias.Size=org.n3r.aoc.file.impl.validators.SizeLimitValidator

alias.DirectOutput=org.n3r.aoc.file.impl.output.DirectOutput
alias.FileOutput=org.n3r.aoc.file.impl.output.FileOutput
alias.SqlOutput=org.n3r.aoc.file.impl.output.SqlOutput

alias.FileInput=org.n3r.aoc.file.impl.input.FileInput
alias.FtpInput=org.n3r.aoc.file.impl.input.FtpInput
alias.SftpInput=org.n3r.aoc.file.impl.input.SftpInput

input=@FileInput(file)

file.path=classpath:org/n3r/aoc/file/test2.txt

output=@DirectOutput

filter=@org.n3r.aoc.file.impl.filter.text.TextFileFilter(text)

text.stat.pos=none

text.data.fields.split=,
text.data.fields.from=ID,PROVINCE_CODE,ACCEPT_DATE,_,INCOME_MONEY,STATE
text.data.fields.from.ID=@Str(4,4)
text.data.fields.from.PROVINCE_CODE=@Int(2)
text.data.fields.from.ACCEPT_DATE=@Date(yyyyMMddHHmmss)
text.data.fields.from.INCOME_MONEY=@Int
text.data.fields.from.STATE=@Enum(10,11)

text.data.fields.to=ID,INCOME_MONEY
```

```java
Properties config = Aocs.loadClasspathProperties("org/n3r/aoc/file/test2.properties");

Processor processor = Processor.fromProperties(config);
processor.process();

DirectOutput output = (DirectOutput) processor.output();
assertThat(output.getContent(), is("ID,INCOME_MONEY\n0001,100\n0002,102\n"));
```

## 订单对账功能
所提供的`外部对账`模型中，涉及`左订单数据(Left)`、`右订单数据(Right)`两个模型。
比对基准为，从`右订单数据(Right)`中取一条订单，从`左订单数据(Left)`中查找订单号相同的订单，比对状态或者金额。
比对差异状态为：
+ `OO`，表示左右订单都有，状态或者金额相同
+ `OR`，表示某条订单右边有，但是左边没有
+ `LO`，表示某条订单左右有，但是右边没有
+ `LR`，表示某条订单左右都有，但是状态或者金额不相同。

运行示例如下：

```ini
alias.OutsideOrderChecker=org.n3r.aoc.check.impl.checker.OutsideOrderChecker
alias.CurrentTimeMillisBatchNoCreator=org.n3r.aoc.check.impl.checker.CurrentTimeMillisBatchNoCreator
alias.SqlDiffOut=org.n3r.aoc.check.impl.diff.SqlDiffOut
alias.DirectRight=org.n3r.aoc.check.impl.right.DirectRight
alias.BdbLeft=org.n3r.aoc.check.impl.left.BdbLeft

model=@OutsideOrderChecker

left=@BdbLeft(bdb)
bdb.dbpath=/var/folders/5d/rnbwp3_s4cj9mskkjl97zjfw0000gn/T/aoc-23412
bdb.dbname=4l8dkZ6JT2

right=@DirectRight(ID,PAY,STATE\na,100,00\na1,200,00)

compareKey=A\:ID
compareFields=C\:PAY,D\:STATE
diffOut=@SqlDiffOut(SqlDiffOut,org/n3r/aoc/checker/AocSqlDiffOut.eql, aoc.diff.detail, aoc.diff.main)
batchNoCreator=@CurrentTimeMillisBatchNoCreator
```

```java
Properties config = Aocs.loadClasspathProperties("org/n3r/aoc/file/checker.properties");
OrderChecker orderChecker = OrderChecker.fromProperties(config);
orderChecker.check();
```

## `SqlDiffOut`比对差异输出到数据库中。
Oracle数据库建库脚本可以[点击这里](https://github.com/bingoohuang/aoc/blob/master/src/main/resources/org/n3r/aoc/checker/aoc_tabls_ora.sql)。
差异结果样例:
> AOC_DIFF

|BATCHNO          |LOS|ORS|LRS|BAL|TOTALLINE|    STARTTIME      |    ENDTIME        |COSTTIME|ACCOUNTDAY|ACCOUNTTYPE|
|-------          |---|---|---|---|---------|  ---------        |    ------         |--------|----------|-----------|
|20131220100441184|1  |  1|  2|  0|        4|`2013-12-20 10:04:41`|`2013-12-20 10:04:41`|0       |NULL      |NULL       |

> AOC_DIFF_DETAIL

BATCHNO|DIFFTYPE|LEFTDATA|RIGHTDATA|DIFF|ORDERNO|DIFFCODE
:-----:|:------:|:------:|:-------:|:--:|:-----:|:------:
20131220100441184|LR|`{"A":"a1","C":"203","D":"01"}`|`{"ID":"a1","PAY":"200","STATE":"00"}`|  `{"C-PAY":"203-200","D-STATE":"01-00"}`|a1|11
20131220100441184|0R|NULL|`{"ID":"a4","PAY":"300","STATE":"00"}`|NULL|a4|NULL
20131220100441184|LR|`{"A":"a","C":"101","D":"01"}`|`{"ID":"a","PAY":"100","STATE":"00"}`|`{"C-PAY":"101-100","D-STATE":"01-00"}`|a|11
20131220100441184|L0|`{"A":"a2","C":"300","D":"03"}`|NULL|NULL|a2|NULL

