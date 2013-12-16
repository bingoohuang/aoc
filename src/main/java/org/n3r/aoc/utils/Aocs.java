package org.n3r.aoc.utils;

import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;
import org.n3r.aoc.ConfigLoadable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Aocs {
    public static Properties loadClasspathProperties(String resourceName) {
        InputStream is = getClasspathResourceInputStream(resourceName);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(resourceName + " can not loaded to properties");
        }


        return properties;
    }

    public static InputStream getClasspathResourceInputStream(String resourceName) {
        InputStream is = Aocs.class.getClassLoader().getResourceAsStream(resourceName);
        if (is == null) {
            throw new RuntimeException(resourceName + " is not available from classpath");
        }
        return is;
    }

    public static <T> T loadObject(Properties properties, String config) {
        String classNameWithPrefix = config;
        if (classNameWithPrefix.startsWith("@")) classNameWithPrefix = classNameWithPrefix.substring(1);
        int leftBracketPos = classNameWithPrefix.indexOf('(');
        String clasName;
        String prefix;
        if (leftBracketPos > 0) {
            int rightBracketPos = classNameWithPrefix.indexOf(')', leftBracketPos);
            if (rightBracketPos < 0) throw new RuntimeException("there is no matched brackets in " + config);
            clasName = classNameWithPrefix.substring(0, leftBracketPos);
            prefix = classNameWithPrefix.substring(leftBracketPos + 1, rightBracketPos);
        } else {
            clasName = classNameWithPrefix;
            prefix = "";
        }

        T obj = Reflect.on(clasName).create().get();
        if (obj instanceof ConfigLoadable) {
            ((ConfigLoadable)obj).loadConfig(subProperties(properties, prefix));
        }

        return obj;
    }

    public static Properties subProperties(Properties properties, String prefix) {
        if (StringUtils.isEmpty(prefix)) return properties;

        Properties newProperties = new Properties();
        String fullPrefix = prefix + ".";
        int prefixSize = fullPrefix.length();
        for(String key : properties.stringPropertyNames()) {
            if (key.indexOf(fullPrefix) == 0) {
                String newKey = key.substring(prefixSize);
                if (StringUtils.isEmpty(newKey)) continue;

                newProperties.put(newKey, properties.getProperty(key));
            }
        }

        return newProperties;
    }
}
