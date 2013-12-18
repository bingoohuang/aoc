package org.n3r.aoc.file.impl.validators;

import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.file.impl.filter.text.TextFileFieldValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateValidator implements TextFileFieldValidator, SimpleConfigAware {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object validate(Object convertedValue, String fieldStringValue) {
        if (StringUtils.isEmpty(fieldStringValue)) return null;

        try {
            return format.parse(fieldStringValue);
        } catch (ParseException e) {
            throw new RuntimeException(fieldStringValue + " can not parsed as " + format);
        }
    }

    @Override
    public void setSimpleConfig(String config) {
        if (StringUtils.isNotEmpty(config)) format = new SimpleDateFormat(config);
    }
}
