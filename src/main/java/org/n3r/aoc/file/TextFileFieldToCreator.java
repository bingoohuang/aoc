package org.n3r.aoc.file;

import java.util.Map;

public interface TextFileFieldToCreator {
    String create(String value, Map<String, TextFieldValue> fieldsValueMap);
}
