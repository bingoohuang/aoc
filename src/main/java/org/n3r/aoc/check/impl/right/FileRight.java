package org.n3r.aoc.check.impl.right;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.check.Order;
import org.n3r.aoc.check.Right;
import org.n3r.aoc.check.impl.order.RecordOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static org.n3r.aoc.utils.Aocs.checkRequired;
import static org.n3r.aoc.utils.Aocs.substitute;

public class FileRight implements Right, SimpleConfigAware {
    private String fileName;
    Logger logger = LoggerFactory.getLogger(FileRight.class);
    private String realFile;

    private FileInputStream fos;
    private Charset charset = Charsets.UTF_8;
    private BufferedReader reader;
    private String[] fieldsName;

    @Override
    public Order next() {
        try {
            String valueLine = reader.readLine();
            if (valueLine == null) return null;

            String[] fieldsValue = Iterables.toArray(Splitter.on(',').split(valueLine), String.class);

            return new RecordOrder(fieldsValue, fieldsName, fieldsValue[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startup(AocContext aocContext) {
        this.realFile = substitute(aocContext, fileName);
        logger.info("startup with {}", realFile);

        try {
            fos = new FileInputStream(realFile);
            reader = new BufferedReader(new InputStreamReader(fos, charset));

            String headLine = reader.readLine();
            fieldsName = Iterables.toArray(Splitter.on(',').split(headLine), String.class);

        } catch (Exception e) {
            logger.error("error to open file {} for read", realFile, e);
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void shutdown() {
        IOUtils.closeQuietly(reader);
        IOUtils.closeQuietly(fos);
        logger.info("shutdown with {}", realFile);
    }

    @Override
    public void setSimpleConfig(String config) {
        this.fileName = config;
        checkRequired(fileName, "right file name");
    }
}
