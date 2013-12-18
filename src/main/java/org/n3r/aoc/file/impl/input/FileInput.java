package org.n3r.aoc.file.impl.input;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.file.Input;
import org.n3r.aoc.utils.Aocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class FileInput implements Input, PropertiesAware {
    private String path;
    private InputStream inputStream;

    Logger logger = LoggerFactory.getLogger(FileInput.class);

    @Override
    public InputStream read(AocContext aocContext) {
        return inputStream;
    }

    @Override
    public void setProperties(Properties rootProperties, Properties properties) {
        path = properties.getProperty("path");
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("path config is required for FileInput");
        }
    }

    @Override
    public void startup(AocContext aocContext) {
        logger.info("startup to read file {}", path);
        if (path.indexOf("classpath:") == 0) {
            String classpath = path.substring("classpath:".length());
            this.inputStream = Aocs.classResourceToInputStream(classpath, false);
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
        logger.info("shutdown to read file {}", path);
    }
}
