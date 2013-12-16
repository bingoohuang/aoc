package org.n3r.aoc.file.impl;

import org.apache.commons.io.IOUtils;
import org.n3r.aoc.ConfigLoadable;
import org.n3r.aoc.file.Input;

import java.io.InputStream;
import java.util.Properties;

public class DirectInput implements Input, ConfigLoadable {
    private String directContent;

    public DirectInput() {
    }


    @Override
    public InputStream read() {
        return IOUtils.toInputStream(directContent);
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void loadConfig(Properties properties) {
        this.directContent = properties.getProperty("text");
    }
}
