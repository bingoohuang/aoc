package org.n3r.aoc.file.impl.filter.text;

import org.n3r.aoc.SimpleConfigAware;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AccumulateStatValidator implements TextFileStatValidator, SimpleConfigAware {
    private long targetValue;
    private long value;
    private String fieldName;

    @Override
    public void setTargetStatValue(String targetStatValue) {
        this.targetValue = Long.parseLong(targetStatValue);
    }

    @Override
    public void checkStat() {
        if (targetValue == value) return;

        throw new RuntimeException("expected sum is " + targetValue + ", but the real sum is " + value);
    }

    @Override
    public void accumulate(Map<String, TextFieldValue> fieldValue) {
        TextFieldValue textFieldValue = fieldValue.get(fieldName);
        if (textFieldValue == null) throw new RuntimeException(fieldName + "'s value is unavailable");

        value += Long.parseLong(textFieldValue.getValue());
        if (value > targetValue)
            throw new RuntimeException("expected sum is " + targetValue + ", but the real sum is at least " + value);
    }

    @Override
    public void setSimpleConfig(String config) {
        if (isEmpty(config)) throw new RuntimeException("AccumulateStatValidator should be bound to a field name");
        this.fieldName = config;
    }
}
