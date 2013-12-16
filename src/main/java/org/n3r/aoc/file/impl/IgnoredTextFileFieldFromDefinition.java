package org.n3r.aoc.file.impl;

public class IgnoredTextFileFieldFromDefinition extends TextFileFieldFromDefinition {
    public IgnoredTextFileFieldFromDefinition(String fieldName) {
        super(fieldName, null);
    }

    @Override
    public Object validate(String fieldStringValue) {
        return fieldStringValue;
    }
}
