package org.n3r.aoc.file.impl.input;

import org.apache.commons.io.IOUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.file.Input;

import java.io.InputStream;
import java.util.Properties;

public class DirectInput implements Input, PropertiesAware {
    private String directContent;

    public DirectInput() {
    }


    @Override
    public InputStream read(AocContext aocContext) {
        return IOUtils.toInputStream(directContent);
    }

    @Override
    public void startup(AocContext aocContext) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void setProperties(Properties rootProperties, Properties properties) {
        this.directContent = properties.getProperty("text");
    }
}
