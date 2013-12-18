package org.n3r.aoc.file.impl.filter.text;

import java.util.Map;

public class LineStatValidator implements TextFileStatValidator {
    private int targetValue;
    private int value;

    @Override
    public void setTargetStatValue(String targetStatValue) {
        targetValue = Integer.parseInt(targetStatValue);
    }

    @Override
    public void checkStat() {
        if (targetValue == value) return;

        throw new RuntimeException("expected lines is " + targetValue + ", but the real lines is " + value);
    }

    @Override
    public void accumulate(Map<String, TextFieldValue> fieldValue) {
        ++value;

        if (value > targetValue)
            throw new RuntimeException("expected lines is " + targetValue + ", but the real lines at least " + value);
    }
}
