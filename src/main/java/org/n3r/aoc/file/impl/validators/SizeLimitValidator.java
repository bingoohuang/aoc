package org.n3r.aoc.file.impl.validators;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.file.impl.filter.text.TextFileFieldValidator;

import java.util.List;

public class SizeLimitValidator implements TextFileFieldValidator, SimpleConfigAware {
    private int minLength = 0;
    private int maxLength = Integer.MAX_VALUE;

    @Override
    public Object validate(Object convertedValue, String fieldStringValue) {
        int length = fieldStringValue.length();
        if (length >= minLength && length <= maxLength) return fieldStringValue;

        throw new RuntimeException("value [" + fieldStringValue + "]'s length is not in range ("
                + minLength + ", " + maxLength + ")");
    }

    @Override
    public void setSimpleConfig(String config) {
        if (StringUtils.isEmpty(config)) return;

        List<String> parts = Splitter.on(',').trimResults().splitToList(config);
        if (parts.size() > 2) {
            throw new RuntimeException(config + "has too many paramerters for StrValidator");
        }

        if (parts.size() == 1) {
            if (StringUtils.isNotEmpty(parts.get(0))) {
                maxLength = Integer.parseInt(parts.get(0));
            }
        } else if (parts.size() == 2) {
            if (StringUtils.isNotEmpty(parts.get(0))) {
                minLength = Integer.parseInt(parts.get(0));
            }
            if (StringUtils.isNotEmpty(parts.get(1))) {
                maxLength = Integer.parseInt(parts.get(1));
            }
        }

        if (minLength > maxLength) {
            throw new RuntimeException(config + "is invalid for StrValidator");
        }
    }
}
