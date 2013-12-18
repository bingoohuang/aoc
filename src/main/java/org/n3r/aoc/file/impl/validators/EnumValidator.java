package org.n3r.aoc.file.impl.validators;

import com.google.common.base.Splitter;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.file.impl.filter.text.TextFileFieldValidator;

import java.util.List;

public class EnumValidator implements TextFileFieldValidator, SimpleConfigAware {
    private List<String> enums;

    @Override
    public Object validate(Object convertedValue, String fieldStringValue) {
        if (enums.contains(fieldStringValue)) return convertedValue;

        throw new RuntimeException("value in not one of " + enums);
    }

    @Override
    public void setSimpleConfig(String config) {
        enums = Splitter.on(',').trimResults().splitToList(config);
    }
}
