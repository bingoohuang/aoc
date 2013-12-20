package org.n3r.aoc.check;

import org.junit.Test;
import org.n3r.aoc.check.impl.checker.CurrentTimeMillisBatchNoCreator;
import org.n3r.aoc.check.impl.checker.OutsideOrderChecker;
import org.n3r.aoc.check.impl.diff.SqlDiffOut;
import org.n3r.aoc.check.impl.left.BdbLeft;
import org.n3r.aoc.check.impl.right.DirectRight;
import org.n3r.aoc.file.Processor;
import org.n3r.aoc.file.TestUtils;
import org.n3r.aoc.file.impl.filter.text.TextFileFilter;
import org.n3r.aoc.file.impl.input.DirectInput;
import org.n3r.aoc.file.impl.output.BdbOutput;

import java.util.Properties;

public class SqlDiffOutTest {
    @Test
    public void test1() {
        Properties processorConfig = new Properties();
        processorConfig.put("input", "@" + DirectInput.class.getName() + "(context)");
        processorConfig.put("context.text", "a,b,101,01\na1,b1,203,01\na2,b2,300,03");

        processorConfig.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        processorConfig.put("text.data.fields.split", ",");
        processorConfig.put("text.data.fields.from", "A,B,C,D");
        processorConfig.put("text.data.fields.to", "A,C,D");

        processorConfig.put("output", "@" + BdbOutput.class.getName() + "(bdb)");
        String dbPath = TestUtils.tempDir().getAbsolutePath();
        processorConfig.put("bdb.dbpath", dbPath);
        String dbName = TestUtils.randomString(10);
        processorConfig.put("bdb.dbname", dbName);

        Processor processor = Processor.fromProperties(processorConfig);
        processor.process();

        Properties checkerConfig = new Properties();
        checkerConfig.put("alias." + OutsideOrderChecker.class.getSimpleName(), OutsideOrderChecker.class.getName());
        checkerConfig.put("alias." + BdbLeft.class.getSimpleName(), BdbLeft.class.getName());
        checkerConfig.put("alias." + DirectRight.class.getSimpleName(), DirectRight.class.getName());
        checkerConfig.put("alias." + CurrentTimeMillisBatchNoCreator.class.getSimpleName(), CurrentTimeMillisBatchNoCreator.class.getName());
        checkerConfig.put("alias." + SqlDiffOut.class.getSimpleName(), SqlDiffOut.class.getName());


        checkerConfig.put("model", "@" + OutsideOrderChecker.class.getSimpleName());

        checkerConfig.put("left", "@" + BdbLeft.class.getSimpleName() + "(bdb)");
        checkerConfig.put("bdb.dbpath", dbPath);
        checkerConfig.put("bdb.dbname", dbName);

        checkerConfig.put("right", "@" + DirectRight.class.getSimpleName() + "(ID,PAY,STATE\na,100,00\na1,200,00\na4,300,00)");
        checkerConfig.put("batchNoCreator", "@" + CurrentTimeMillisBatchNoCreator.class.getSimpleName());

        checkerConfig.put("compareFields", "C:PAY,D:STATE");
        checkerConfig.put("diffOut", "@" + SqlDiffOut.class.getSimpleName() + "(SqlDiffOut,org/n3r/aoc/checker/AocSqlDiffOut.eql, aoc.diff.detail, aoc.diff.main)");

        OrderChecker orderChecker = OrderChecker.fromProperties(checkerConfig);
        orderChecker.check();
    }
}
