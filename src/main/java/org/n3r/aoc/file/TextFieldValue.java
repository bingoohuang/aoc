package org.n3r.aoc.file;

import org.n3r.aoc.file.impl.TextFileFieldFromDefinition;

public class TextFieldValue {
    private final String fieldStringValue;
    private final TextFileFieldFromDefinition textFileFieldFromDefinition;
    private Object convertedValue;
    private String value;

    public TextFieldValue(String fieldStringValue, TextFileFieldFromDefinition textFileFieldFromDefinition) {
        this.fieldStringValue = fieldStringValue;
        this.textFileFieldFromDefinition = textFileFieldFromDefinition;
    }

    public void validate() {
        convertedValue = textFileFieldFromDefinition.validate(fieldStringValue);
    }

    public String getValue() {
        return fieldStringValue;
    }
}
