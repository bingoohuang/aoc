package org.n3r.aoc.file;

import org.junit.Test;
import org.n3r.aoc.file.impl.output.DirectOutput;
import org.n3r.aoc.utils.Aocs;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Properties2Test {
    @Test
    public void test() {
        Properties config = Aocs.loadClasspathProperties("org/n3r/aoc/file/test2.properties");

        Processor processor = Processor.fromProperties(config);
        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("ID,INCOME_MONEY\n0001,100\n0002,102\n"));
    }
}
