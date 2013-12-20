package org.n3r.aoc.file.impl.output;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.file.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.n3r.aoc.utils.Aocs.checkRequired;
import static org.n3r.aoc.utils.Aocs.substitute;

public class FileOutput implements Output, SimpleConfigAware {
    Logger logger = LoggerFactory.getLogger(FileOutput.class);
    private String file;
    private String realFile;

    private FileOutputStream fos;
    private PrintStream ps;
    private String charset = "UTF-8";

    @Override
    public void write(List<String> toFieldsValue) {
        writeFieldsName(toFieldsValue);
    }

    @Override
    public void writeFieldsName(List<String> toFieldNames) {
        boolean firstField = true;
        for (String field : toFieldNames) {
            if (!firstField) ps.print(',');
            else firstField = false;

            ps.print(field);
        }
        ps.println();
    }

    @Override
    public void startup(AocContext aocContext) {
        this.realFile = substitute(aocContext, file);
        logger.info("startup with {}", realFile);

        try {
            fos = new FileOutputStream(realFile);
            ps = new PrintStream(fos, false, charset);
        } catch (Exception e) {
            logger.error("error to open file {} for write", realFile, e);
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void shutdown() {
        IOUtils.closeQuietly(ps);
        IOUtils.closeQuietly(fos);
        logger.info("shutdown with {}", realFile);
    }

    @Override
    public void setSimpleConfig(String config) {
        this.file = config;
        checkRequired(file, "output file name");
    }
}
