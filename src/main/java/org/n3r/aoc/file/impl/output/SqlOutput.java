package org.n3r.aoc.file.impl.output;

import com.google.common.base.Splitter;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.file.Output;
import org.n3r.eql.Eql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SqlOutput implements Output, SimpleConfigAware {
    Logger logger = LoggerFactory.getLogger(SqlOutput.class);
    private String configName;
    private String sqlFile;
    private String sqlid;

    @Override
    public void write(List<String> toFieldsValue) {
        Object[] params = toFieldsValue.toArray(new String[0]);
        new Eql(configName).useSqlFile(sqlFile).id(sqlid).params(params).execute();
    }

    @Override
    public void writeFieldsName(List<String> toFieldNames) {
    }

    @Override
    public void startup(AocContext aocContext) {
        logger.info("startup with config name {}, sql file {}, sql id {}", configName, sqlFile, sqlid);
    }

    @Override
    public void shutdown() {
        logger.info("shutdown with config name {}, sql file {}, sql id {}", configName, sqlFile, sqlid);
    }

    @Override
    public void setSimpleConfig(String config) {
        List<String> strings = Splitter.on(',').trimResults().splitToList(config);
        // configName, SqlFile, sqlid
        if (strings.size() != 3) {
            throw new RuntimeException("sql config need configName, sqlFile and sqlid");
        }

        configName = strings.get(0);
        sqlFile = strings.get(1);
        sqlid = strings.get(2);
    }
}
