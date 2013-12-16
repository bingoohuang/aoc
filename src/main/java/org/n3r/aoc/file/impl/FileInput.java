package org.n3r.aoc.file.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.ConfigLoadable;
import org.n3r.aoc.file.Input;
import org.n3r.aoc.utils.Aocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class FileInput implements Input, ConfigLoadable {
    private String path;
    private InputStream inputStream;

    @Override
    public InputStream read() {
        return inputStream;
    }

    @Override
    public void loadConfig(Properties properties) {
        path = properties.getProperty("path");
    }

    @Override
    public void startup() {
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("path is empty");
        }

        if (path.indexOf("classpath:") == 0) {
            String classpath = path.substring("classpath:".length());
            this.inputStream = Aocs.getClasspathResourceInputStream(classpath);
        } else {
            File file = new File(path);
            if (!file.exists()) {
                throw new RuntimeException("file does not exists : " + path);
            }
            try {
                this.inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("file inputstream error : " + path, e);
            }
        }
    }

    @Override
    public void shutdown() {
        IOUtils.closeQuietly(inputStream);
    }
}
