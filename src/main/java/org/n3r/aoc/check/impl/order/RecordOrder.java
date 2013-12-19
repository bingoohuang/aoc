package org.n3r.aoc.check.impl.order;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.n3r.aoc.check.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecordOrder implements Order {
    private final Object[] fieldsValue;
    private final String[] fieldsName;
    private String keyValue;

    public RecordOrder(Object[] fieldsValue, String[] fieldsName, String keyValue) {
        this.fieldsValue = fieldsValue;
        this.fieldsName = fieldsName;
        this.keyValue = keyValue;
    }

    public RecordOrder(List<String> fieldsValue, List<String> fieldsName, String keyValue) {
        this(fieldsValue.toArray(new String[0]), fieldsName.toArray(new String[0]), keyValue);
    }

    @Override
    public String keyValue() {
        return keyValue;
    }

    @Override
    public Object fieldValue(String fieldName) {
        for (int i = 0; i < fieldsName.length; ++i) {
            if (fieldsName[i].equals(fieldName)) return fieldsValue[i];
        }

        return null;
    }

    @Override
    public String toString() {
        Map<String, Object> strMap = Maps.newHashMap();
        for (int i = 0; i < fieldsName.length; ++i) {
            strMap.put(fieldsName[i], fieldsValue[i]);
        }

        return JSON.toJSONString(strMap);
    }
}
