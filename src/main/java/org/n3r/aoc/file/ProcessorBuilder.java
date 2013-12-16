package org.n3r.aoc.file;

import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.utils.Aocs;

import java.util.Properties;

public class ProcessorBuilder {
    private Input input;
    private Filter filter;
    private Output output;

    public ProcessorBuilder fromClasspathProperties(String classpathProperties) {
        Properties config = Aocs.loadClasspathProperties(classpathProperties);

        return fromProperties(config);
    }

    public ProcessorBuilder fromProperties(Properties config) {
        input = loadInput(config);
        filter = loadFilter(config);
        output = loadOutput(config);

        return this;
    }

    private Output loadOutput(Properties config) {
        String outputConfig = config.getProperty("output");
        if (StringUtils.isEmpty(outputConfig)) {
            throw new RuntimeException("output is not defined in properties.");
        }

        return Aocs.loadObject(config, outputConfig);
    }

    private Filter loadFilter(Properties config) {
        String filterConfig = config.getProperty("filter");
        if (StringUtils.isEmpty(filterConfig)) {
            throw new RuntimeException("filter is not defined in properties.");
        }

        return Aocs.loadObject(config, filterConfig);
    }

    private Input loadInput(Properties config) {
        String inputConfig = config.getProperty("input");
        if (StringUtils.isEmpty(inputConfig)) {
            throw new RuntimeException("input is not defined in properties file.");
        }

        return Aocs.loadObject(config, inputConfig);
    }

    public Processor build() {
        return new Processor(input, filter, output);
    }
}
