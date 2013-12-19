package org.n3r.aoc.check;

import org.junit.Test;
import org.n3r.aoc.check.impl.checker.CurrentTimeMillisBatchNoCreator;
import org.n3r.aoc.check.impl.checker.OutsideOrderChecker;
import org.n3r.aoc.check.impl.diff.ConsoleDiffOut;
import org.n3r.aoc.check.impl.left.BdbLeft;
import org.n3r.aoc.check.impl.right.DirectRight;
import org.n3r.aoc.file.Processor;
import org.n3r.aoc.file.TestUtils;
import org.n3r.aoc.file.impl.filter.text.TextFileFilter;
import org.n3r.aoc.file.impl.input.DirectInput;
import org.n3r.aoc.file.impl.output.BdbOutput;

import java.util.Properties;

public class SimpleOrderCheckerTest {
    @Test
    public void test1() {
        Properties processorConfig = new Properties();
        processorConfig.put("input", "@" + DirectInput.class.getName() + "(context)");
        processorConfig.put("context.text", "a,b,100\na1,b1,200");

        processorConfig.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        processorConfig.put("text.data.fields.split", ",");
        processorConfig.put("text.data.fields.from", "A,B,C");
        processorConfig.put("text.data.fields.to", "A,C");

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
        checkerConfig.put("alias." + ConsoleDiffOut.class.getSimpleName(), ConsoleDiffOut.class.getName());


        checkerConfig.put("model", "@" + OutsideOrderChecker.class.getSimpleName());

        checkerConfig.put("left", "@" + BdbLeft.class.getSimpleName() + "(bdb)");
        checkerConfig.put("bdb.dbpath", dbPath);
        checkerConfig.put("bdb.dbname", dbName);

        checkerConfig.put("right", "@" + DirectRight.class.getSimpleName() + "(ID,PAY\na,100\na1,200)");
        checkerConfig.put("batchNoCreator", "@" + CurrentTimeMillisBatchNoCreator.class.getSimpleName());

        checkerConfig.put("compareKey", "A:ID");
        checkerConfig.put("compareFields", "C:PAY");
        checkerConfig.put("diffOut", "@" + ConsoleDiffOut.class.getSimpleName());

        OrderChecker orderChecker = OrderChecker.fromProperties(checkerConfig);
        orderChecker.check();
    }
}
