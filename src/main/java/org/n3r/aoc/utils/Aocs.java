package org.n3r.aoc.utils;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.check.impl.order.RecordOrder;
import org.n3r.eql.util.EqlUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

public class Aocs {
    public static Properties loadClasspathProperties(String resourceName) {
        InputStream is = classResourceToInputStream(resourceName, false);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(resourceName + " can not loaded to properties");
        }


        return properties;
    }

    /**
     * Return the context classloader. BL: if this is command line operation, the classloading issues are more sane.
     * During servlet execution, we explicitly set the ClassLoader.
     *
     * @return The context classloader.
     */
    public static ClassLoader getClassLoader() {
        return Objects.firstNonNull(
                Thread.currentThread().getContextClassLoader(),
                Aocs.class.getClassLoader());
    }

    public static InputStream classResourceToInputStream(String pathname, boolean silent) {
        InputStream is = classResourceToStream(pathname);
        if (is != null || silent) return is;

        throw new RuntimeException("fail to find " + pathname + " in classpath");
    }

    public static InputStream classResourceToStream(String resourceName) {
        return getClassLoader().getResourceAsStream(resourceName);
    }

    public static <T> T loadObject(Properties rootProperties, Properties properties, String config) {
        String classNameWithPrefix = config;
        if (classNameWithPrefix.startsWith("@")) classNameWithPrefix = classNameWithPrefix.substring(1);
        int leftBracketPos = classNameWithPrefix.indexOf('(');
        String className;
        String prefix;
        if (leftBracketPos > 0) {
            int rightBracketPos = classNameWithPrefix.indexOf(')', leftBracketPos);
            if (rightBracketPos < 0) throw new RuntimeException("there is no matched brackets in " + config);
            className = classNameWithPrefix.substring(0, leftBracketPos);
            prefix = classNameWithPrefix.substring(leftBracketPos + 1, rightBracketPos);
        } else {
            className = classNameWithPrefix;
            prefix = "";
        }

        String aliasKey = "alias." + className;
        if (rootProperties.containsKey(aliasKey)) {
            className = rootProperties.getProperty(aliasKey);
        }

        T obj = Reflect.on(className).create().get();
        if (obj instanceof PropertiesAware) {
            ((PropertiesAware) obj).setProperties(rootProperties, subProperties(properties, prefix));
        }
        if (obj instanceof SimpleConfigAware) {
            ((SimpleConfigAware) obj).setSimpleConfig(prefix);
        }

        return obj;
    }

    public static Properties subProperties(Properties properties, String prefix) {
        if (StringUtils.isEmpty(prefix)) return properties;

        Properties newProperties = new Properties();
        String fullPrefix = prefix + ".";
        int prefixSize = fullPrefix.length();
        for (String key : properties.stringPropertyNames()) {
            if (key.indexOf(fullPrefix) == 0) {
                String newKey = key.substring(prefixSize);
                if (StringUtils.isEmpty(newKey)) continue;

                newProperties.put(newKey, properties.getProperty(key));
            }
        }

        return newProperties;
    }

    /**
     * Create a temporary file
     */
    public static File tempFile() {
        File f;
        try {
            f = File.createTempFile("aoc", ".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        f.deleteOnExit();
        return f;
    }

    public static void sleepMilis(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            // ignore
        }
    }


    public static void checkRequired(String ftpHost, String name) {
        if (StringUtils.isNotEmpty(ftpHost)) return;

        throw new RuntimeException(name + " is requied");
    }

    public static String substitute(AocContext aocContext, String before) {
        return Substituters.parse(before, aocContext.getAocContext());
    }

    public static RecordOrder mapRow(ResultSet rs, int rowNum)  throws SQLException {
            ResultSetMetaData metaData = rs.getMetaData();
            Object[] fieldsValue = new Object[metaData.getColumnCount()];
            String[] fieldsName = new String[metaData.getColumnCount()];
            for (int i = 0, ii = metaData.getColumnCount(); i < ii; ++i) {
                fieldsName[i] = EqlUtils.lookupColumnName(metaData, i + 1);
                fieldsValue[i] = EqlUtils.getResultSetValue(rs, i + 1);
            }

            return new RecordOrder(fieldsValue, fieldsName, null);
    }
}
