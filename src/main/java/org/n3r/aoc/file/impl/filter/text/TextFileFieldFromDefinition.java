package org.n3r.aoc.file.impl.filter.text;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.utils.Aocs;

import java.util.List;
import java.util.Properties;

public class TextFileFieldFromDefinition {
    private final String fieldName;
    private final int fieldIndex;
    private TextFileFieldValidator[] validators;

    public TextFileFieldFromDefinition(String fieldName, int fieldIndex, TextFileFieldValidator[] validators) {
        this.fieldName = fieldName;
        this.fieldIndex = fieldIndex;
        this.validators = validators;
    }

    public static TextFileFieldFromDefinition createField(Properties rootProperties, Properties properties,
                                                          String fieldName, int fieldIndex) {
        if (fieldName.equals("") || fieldName.equals("_")) {
            return new IgnoredTextFileFieldFromDefinition(fieldName);
        }

        String fieldValidators = properties.getProperty(fieldName);
        List<TextFileFieldValidator> validators = Lists.newArrayList();

        if (StringUtils.isNotEmpty(fieldValidators)) {
            Iterable<String> validatorsNames = Splitter.on('@').omitEmptyStrings().trimResults().split(fieldValidators);
            for (String validatorName : validatorsNames) {
                validators.add((TextFileFieldValidator) Aocs.loadObject(rootProperties, properties, validatorName));
            }
        }

        return new TextFileFieldFromDefinition(fieldName, fieldIndex, validators.size() == 0 ? null
                : validators.toArray(new TextFileFieldValidator[validators.size()]));
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public TextFieldValue createValue(String fieldStringValue) {
        return new TextFieldValue(fieldStringValue, this);
    }

    public Object validate(String fieldStringValue) {
        if (validators == null) return fieldStringValue;

        Object convertedValue = fieldStringValue;
        for (TextFileFieldValidator validator : validators) {
            convertedValue = validator.validate(convertedValue, fieldStringValue);
        }
        return convertedValue;
    }
}
