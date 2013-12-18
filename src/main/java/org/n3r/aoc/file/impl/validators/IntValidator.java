package org.n3r.aoc.file.impl.validators;

public class IntValidator extends SizeLimitValidator {
    @Override
    public Object validate(Object convertedValue, String fieldStringValue) {
        super.validate(convertedValue, fieldStringValue);

        return Integer.parseInt(fieldStringValue);
    }
}
