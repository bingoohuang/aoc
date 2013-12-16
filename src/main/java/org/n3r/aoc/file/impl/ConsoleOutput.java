package org.n3r.aoc.file.impl;

import org.n3r.aoc.file.Output;

import java.util.List;

public class ConsoleOutput implements Output {
    @Override
    public void write(List<String> toFieldsValue) {
        System.out.println(toFieldsValue);
    }

    @Override
    public void startup() {
        System.out.println("ConsoleOutput startup");
    }

    @Override
    public void shutdown() {
        System.out.println("ConsoleOutput shutdown");
    }
}
