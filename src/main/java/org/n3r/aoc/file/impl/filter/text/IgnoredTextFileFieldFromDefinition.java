package org.n3r.aoc.file.impl.filter.text;

public class IgnoredTextFileFieldFromDefinition extends TextFileFieldFromDefinition {
    public IgnoredTextFileFieldFromDefinition(String fieldName) {
        super(fieldName, 0, null);
    }

    @Override
    public Object validate(String fieldStringValue) {
        return fieldStringValue;
    }
}
