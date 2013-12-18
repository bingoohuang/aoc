package org.n3r.aoc.file.impl.validators;

import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.file.impl.filter.text.TextFileFieldValidator;

public class RequiredValidator implements TextFileFieldValidator {
    @Override
    public Object validate(Object convertedValue, String fieldStringValue) {
        if (StringUtils.isEmpty(fieldStringValue))
            throw new RuntimeException("required check failed");

        return fieldStringValue;
    }
}
