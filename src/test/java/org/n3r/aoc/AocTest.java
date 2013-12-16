package org.n3r.aoc;

import org.junit.Test;
import org.n3r.aoc.file.Processor;
import org.n3r.aoc.file.ProcessorBuilder;
import org.n3r.aoc.file.impl.DirectInput;
import org.n3r.aoc.file.impl.DirectOutput;
import org.n3r.aoc.file.impl.TextFileFilter;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AocTest {
    @Test
    public void test1() {
        Properties config = new Properties();
        config.put("input", "@" + DirectInput.class.getName() + "(context)");
        config.put("context.text", "a,b,c,d");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        Processor processor = new ProcessorBuilder().fromProperties(config).build();

        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("a,c\n"));
    }
}
