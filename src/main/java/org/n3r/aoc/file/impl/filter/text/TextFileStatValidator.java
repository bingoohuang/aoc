package org.n3r.aoc.file.impl.filter.text;

import java.util.Map;

public interface TextFileStatValidator {
    void setTargetStatValue(String targetStatValue);

    void checkStat();

    void accumulate(Map<String, TextFieldValue> fieldValue);
}
