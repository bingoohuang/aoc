package org.n3r.aoc.file.impl.filter.text;

public interface TextFileFieldValidator {
    Object validate(Object convertedValue, String fieldStringValue);
}
