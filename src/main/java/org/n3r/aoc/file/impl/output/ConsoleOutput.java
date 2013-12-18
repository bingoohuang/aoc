package org.n3r.aoc.file.impl.output;

import org.n3r.aoc.AocContext;
import org.n3r.aoc.file.Output;

import java.util.List;

public class ConsoleOutput implements Output {
    @Override
    public void write(AocContext aocContext, List<String> toFieldsValue) {
        System.out.println(toFieldsValue);
    }

    @Override
    public void writeFieldsName(List<String> toFieldNames) {
        System.out.println("fields name: " + toFieldNames);
    }

    @Override
    public void startup(AocContext aocContext) {
        System.out.println("ConsoleOutput startup");
    }

    @Override
    public void shutdown() {
        System.out.println("ConsoleOutput shutdown");
    }
}
