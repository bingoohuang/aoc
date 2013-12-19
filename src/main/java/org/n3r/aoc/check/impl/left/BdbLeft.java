package org.n3r.aoc.check.impl.left;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.check.Left;
import org.n3r.aoc.check.Order;
import org.n3r.aoc.check.impl.order.RecordOrder;
import org.n3r.aoc.file.impl.output.BdbOutput;
import org.n3r.aoc.utils.Bdbs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class BdbLeft implements Left, PropertiesAware {
    private Database database;
    private String dbPath;
    private String dbName;

    Logger logger = LoggerFactory.getLogger(BdbLeft.class);
    private List<String> fieldsName;

    @Override
    public Order pop(String keyValue) {
        byte[] key = Bdbs.toBytes(keyValue);
        byte[] bytes = Bdbs.get(database, key);
        if (bytes == null) return null;

        Bdbs.delete(database, key);

        List<String> fieldsValue = JSON.parseArray(Bdbs.toStr(bytes), String.class);

        return new RecordOrder(fieldsValue, fieldsName, keyValue);
    }

    @Override
    public Collection<Order> popAll() {
        List<Order> records = Lists.newArrayList();
        Cursor cursor = Bdbs.openCursor(database);
        DatabaseEntry foundKey = new DatabaseEntry();
        DatabaseEntry foundValue = new DatabaseEntry();
        while (Bdbs.cursorNext(cursor, foundKey, foundValue)) {
            String keyValue = Bdbs.toStr(foundKey.getData());
            String json = Bdbs.toStr(foundValue.getData());
            List<String> fieldsValue = JSON.parseArray(json, String.class);

            records.add(new RecordOrder(fieldsValue, fieldsName, keyValue));
        }
        Bdbs.closeCursor(cursor);

        return records;
    }


    @Override
    public void startup(AocContext aocContext) {
        logger.info("startup to use bdb as left at {} with db name {}", dbPath, dbName);
        database = Bdbs.openDb(dbPath, dbName, false);

        byte[] key = Bdbs.toBytes(BdbOutput.TO_FIELD_NAMES);
        byte[] bytes = Bdbs.get(database, key);
        fieldsName = JSON.parseArray(Bdbs.toStr(bytes), String.class);
        Bdbs.delete(database, key);
    }

    @Override
    public void shutdown() {
        Bdbs.closeDbAndEvn(database);
        database = null;
        logger.info("shutdown to read bdb at {} with db name {}", dbPath, dbName);
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
