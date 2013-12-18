package org.n3r.aoc.utils;

import com.google.common.base.Charsets;
import com.sleepycat.je.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * bdb utilities。
 */
public class Bdbs {
    public static Environment openEnv(File path, long cacheSize) {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true).setCacheSize(cacheSize);
        path.mkdirs();
        return new Environment(path, envConfig);
    }

    public static void closeEnv(Environment env, boolean cleanLog) {
        if (cleanLog && env != null) env.cleanLog();
        IOUtils.closeQuietly(env);
    }

    public static Database openDb(Environment env, String dbName) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        return env.openDatabase(null, dbName, dbConfig);
    }

    public static Database openDb(String envPath, String dbName, boolean cleanDir) {
        if (cleanDir) cleanDir(envPath);

        Environment environment = openEnv(new File(envPath), 1024 * 1024);
        return openDb(environment, dbName);
    }

    public static void closeDbAndEvn(Database database) {
        Environment environment = database.getEnvironment();
        closeDb(database);
        closeEnv(environment, false);
    }

    public static void cleanDir(String dir) {
        cleanDir(new File(dir));
    }

    public static void cleanDir(File path) {
        try {
            if (path.exists()) FileUtils.cleanDirectory(path);
        } catch (IOException e) {
            // Ingore
        }
    }

    public static void closeDb(Database db) {
        IOUtils.closeQuietly(db);
    }

    public static OperationStatus put(Database db, byte[] key, byte[] value) {
        return db.put(null, new DatabaseEntry(key), new DatabaseEntry(value));
    }

    public static OperationStatus put(Database db, String key, String value) {
        return put(db, toBytes(key), toBytes(value));
    }

    public static byte[] toBytes(String value) {
        return value == null ? new byte[0] : value.getBytes(Charsets.UTF_8);
    }

    public static byte[] get(Database db, byte[] key) {
        DatabaseEntry queryKey = new DatabaseEntry(key);
        DatabaseEntry value = new DatabaseEntry();
        OperationStatus status = db.get(null, queryKey, value, LockMode.DEFAULT);
        return status == OperationStatus.SUCCESS ? value.getData() : null;
    }

    public static String get(Database db, String key) {
        byte[] bytes = get(db, toBytes(key));
        return bytes != null ? toStr(bytes) : null;
    }

    private static String toStr(byte[] bytes) {
        return bytes.length == 0 ? null : new String(bytes, Charsets.UTF_8);
    }

    public static boolean delete(Database db, byte[] key) {
        return db.delete(null, new DatabaseEntry(key)) == OperationStatus.SUCCESS;
    }

    public static boolean delete(Database db, String key) {
        return delete(db, toBytes(key));
    }

    public static Cursor openCursor(Database db) {
        return db.openCursor(null, null);
    }

    public static void closeCursor(Cursor cursor) {
        IOUtils.closeQuietly(cursor);
    }

    public static boolean cursorNext(Cursor cursor, DatabaseEntry foundKey, DatabaseEntry foundValue) {
        OperationStatus next = cursor.getNext(foundKey, foundValue, LockMode.DEFAULT);
        return next == OperationStatus.SUCCESS;
    }

}