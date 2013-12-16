package org.n3r.aoc.file;

import org.apache.commons.io.IOUtils;
import org.n3r.aoc.LifeCycle;

import java.io.InputStream;

public class Processor implements LifeCycle {
    private Input input;
    private Filter filter;
    private Output output;

    public Processor() {
    }

    public Processor(Input input, Filter filter, Output output) {
        this.input = input;
        this.filter = filter;
        this.output = output;
    }

    public Processor input(Input input) {
        this.input = input;
        return this;
    }

    public Processor filter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public Processor output(Output output) {
        this.output = output;
        return this;
    }

    public void process() {
        startup();
        InputStream is = null;
        try {
            is = input.read();
            filter.filter(is, output);
        } finally {
            IOUtils.closeQuietly(is);
            shutdown();
        }
    }

    @Override
    public void startup() {
        input.startup();
        filter.startup();
        output.startup();
    }

    @Override
    public void shutdown() {
        input.shutdown();
        filter.shutdown();
        output.shutdown();
    }

    public Output output() {
        return output;
    }
}
