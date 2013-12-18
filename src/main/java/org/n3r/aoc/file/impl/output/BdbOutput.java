package org.n3r.aoc.file.impl.output;

import com.alibaba.fastjson.JSON;
import com.sleepycat.je.Database;
import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.file.Output;
import org.n3r.aoc.utils.Bdbs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class BdbOutput implements Output, PropertiesAware {
    public static final String TO_FIELD_NAMES = "__ToFieldNames__";

    private Database database;
    private String dbPath;
    private String dbName;

    Logger logger = LoggerFactory.getLogger(BdbOutput.class);

    @Override
    public void write(AocContext aocContext, List<String> toFieldsValue) {
        String key = toFieldsValue.get(0);
        Bdbs.put(database, Bdbs.toBytes(key), toBytes(toFieldsValue));
    }

    @Override
    public void writeFieldsName(List<String> toFieldNames) {
        Bdbs.put(database, Bdbs.toBytes(TO_FIELD_NAMES), toBytes(toFieldNames));
    }

    private byte[] toBytes(List<String> toFieldsValue) {
        return Bdbs.toBytes(JSON.toJSONString(toFieldsValue));
    }

    @Override
    public void startup(AocContext aocContext) {
        logger.info("startup to write bdb at {} with db name {}", dbPath, dbName);
        database = Bdbs.openDb(dbPath, dbName, false);
    }

    @Override
    public void shutdown() {
        Bdbs.closeDbAndEvn(database);
        database = null;
        logger.info("shutdown to write bdb at {} with db name {}", dbPath, dbName);
    }

    @Override
    public void setProperties(Properties rootProperties, Properties properties) {
        this.dbPath = properties.getProperty("dbpath");
        this.dbName = properties.getProperty("dbname");

        if (StringUtils.isEmpty(dbPath) || StringUtils.isEmpty(dbName)) {
            throw new RuntimeException("dbpath and dbname are all required");
        }
    }
}
