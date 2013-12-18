package org.n3r.aoc.file;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.n3r.aoc.AocContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Processor {
    private AocContext aocContext;
    private Input input;
    private Filter filter;
    private Output output;

    Logger logger = LoggerFactory.getLogger(Processor.class);


    public Processor(Input input, Filter filter, Output output, AocContext aocContext) {
        this.input = input;
        this.filter = filter;
        this.output = output;
        this.aocContext = aocContext == null ? new AocContext() : aocContext;
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

    public Processor aocContext(AocContext aocContext) {
        this.aocContext = aocContext;
        return this;
    }

    public void process() {
        startup(aocContext);
        InputStream is = null;
        try {
            is = input.read(aocContext);
            filter.filter(aocContext, is, output);
        } catch (Exception e) {
            logger.error("process error", e);
            throw Throwables.propagate(e);
        } finally {
            IOUtils.closeQuietly(is);
            shutdown();
        }
    }

    private void startup(AocContext aocContext) {
        logger.info("start to process");
        input.startup(aocContext);
        filter.startup(aocContext);
        output.startup(aocContext);
    }

    private void shutdown() {
        logger.info("end to process");
        input.shutdown();
        filter.shutdown();
        output.shutdown();
    }

    public Output output() {
        return output;
    }

    public Input input() {
        return input;
    }

    public Filter filter() {
        return filter;
    }

    public AocContext aocContext() {
        return aocContext;
    }
}
