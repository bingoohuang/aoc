package org.n3r.aoc.file.impl.output;

import com.google.common.base.Joiner;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.file.Output;

import java.util.List;

public class DirectOutput implements Output {
    StringBuilder content;
    private Joiner joiner = Joiner.on(',');

    @Override
    public void write(List<String> toFieldsValue) {
        content.append(joiner.join(toFieldsValue)).append("\n");
    }

    @Override
    public void writeFieldsName(List<String> toFieldNames) {
        content.append(joiner.join(toFieldNames)).append("\n");
    }

    @Override
    public void startup(AocContext aocContext) {
        content = new StringBuilder();
    }

    @Override
    public void shutdown() {

    }

    public String getContent() {
        return content.toString();
    }
}
