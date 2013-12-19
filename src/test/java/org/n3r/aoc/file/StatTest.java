package org.n3r.aoc.file;

import org.junit.Test;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.file.impl.filter.text.AccumulateStatValidator;
import org.n3r.aoc.file.impl.filter.text.LineStatValidator;
import org.n3r.aoc.file.impl.filter.text.TextFileFilter;
import org.n3r.aoc.file.impl.input.DirectInput;
import org.n3r.aoc.file.impl.output.DirectOutput;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StatTest {
    @Test
    public void directInDirectOut() {
        Properties config = new Properties();

        config.put("alias." + LineStatValidator.class.getSimpleName(), LineStatValidator.class.getName());
        config.put("alias." + AccumulateStatValidator.class.getSimpleName(), AccumulateStatValidator.class.getName());
        config.put("alias." + DirectInput.class.getSimpleName(), DirectInput.class.getName());

        config.put("input", "@" + DirectInput.class.getSimpleName() + "(context)");
        config.put("context.text", "2,300\na,b,100,d\na1,b1,200,d1");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");


        config.put("text.stat.pos", "first");
        config.put("text.stat.split", ",");
        config.put("text.stat.fields.define", "lineCount,moneySum");
        config.put("text.stat.fields.lineCount", "@LineStatValidator");
        config.put("text.stat.fields.moneySum", "@AccumulateStatValidator(C)");

        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C,D");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        AocContext aocContext = new AocContext();

        Processor processor =  Processor.fromProperties(config).aocContext(aocContext);

        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("A,C\na,100\na1,200\n"));
    }

    @Test(expected = RuntimeException.class)
    public void directInDirectOut2() {
        Properties config = new Properties();

        config.put("alias." + LineStatValidator.class.getSimpleName(), LineStatValidator.class.getName());
        config.put("alias." + AccumulateStatValidator.class.getSimpleName(), AccumulateStatValidator.class.getName());
        config.put("alias." + DirectInput.class.getSimpleName(), DirectInput.class.getName());

        config.put("input", "@" + DirectInput.class.getSimpleName() + "(context)");
        config.put("context.text", "1,350\na,b,100,d\na1,b1,200,d1");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");


        config.put("text.stat.pos", "first");
        config.put("text.stat.split", ",");
        config.put("text.stat.fields.define", "lineCount,moneySum");
        config.put("text.stat.fields.lineCount", "@LineStatValidator");
        config.put("text.stat.fields.moneySum", "@AccumulateStatValidator(C)");

        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C,D");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        AocContext aocContext = new AocContext();

        Processor processor = Processor.fromProperties(config).aocContext(aocContext);

        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("a,100\na1,200\n"));
    }
}
