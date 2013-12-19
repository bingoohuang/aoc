package org.n3r.aoc.file;

import org.junit.Test;
import org.n3r.aoc.file.impl.filter.text.TextFileFilter;
import org.n3r.aoc.file.impl.input.DirectInput;
import org.n3r.aoc.file.impl.output.DirectOutput;
import org.n3r.aoc.file.impl.validators.*;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ValidatorsTest {
    @Test
    public void directInDirectOut() {
        Properties config = new Properties();
        config.put("alias.Str", StrValidator.class.getName());
        config.put("alias.Int", IntValidator.class.getName());
        config.put("alias.Date", DateValidator.class.getName());
        config.put("alias.Enum", EnumValidator.class.getName());
        config.put("alias.Required", RequiredValidator.class.getName());
        config.put("alias.Size", SizeLimitValidator.class.getName());

        config.put("input", "@" + DirectInput.class.getName() + "(context)");
        config.put("context.text", "a,b,c,d\na1,b1,c1");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C");
        config.put("text.data.fields.from.A", "@Str(1,2)");
        config.put("text.data.fields.from.C", "@Str(1,2)");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        Processor processor = Processor.fromProperties(config);

        processor.process();

        DirectOutput output = (DirectOutput) processor.output();
        assertThat(output.getContent(), is("A,C\na,c\na1,c1\n"));
    }

    @Test(expected = RuntimeException.class)
    public void invalide() {
        Properties config = new Properties();
        config.put("alias.Str", StrValidator.class.getName());
        config.put("alias.Int", IntValidator.class.getName());
        config.put("alias.Date", DateValidator.class.getName());
        config.put("alias.Enum", EnumValidator.class.getName());
        config.put("alias.Required", RequiredValidator.class.getName());
        config.put("alias.Size", SizeLimitValidator.class.getName());

        config.put("input", "@" + DirectInput.class.getName() + "(context)");
        config.put("context.text", "a,b,c,d\na1,b1,c1");

        config.put("filter", "@" + TextFileFilter.class.getName() + "(text)");
        config.put("text.data.fields.split", ",");
        config.put("text.data.fields.from", "A,B,C");
        config.put("text.data.fields.from.A", "@Str(3,)");
        config.put("text.data.fields.to", "A,C");

        config.put("output", "@" + DirectOutput.class.getName());

        Processor processor = Processor.fromProperties(config);

        processor.process();
    }
}
