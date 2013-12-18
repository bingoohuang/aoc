package org.n3r.aoc.file.impl.filter.text;

import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.n3r.aoc.utils.Aocs.loadObject;

public class TextFileFieldToDefinition {
    private final String fieldName;
    private final TextFileFieldToCreator creator;

    public TextFileFieldToDefinition(String fieldName, TextFileFieldToCreator creator) {
        this.fieldName = fieldName;
        this.creator = creator;
    }

    public static TextFileFieldToDefinition createField(Properties rootProperties, Properties properties, String fieldName, Map<String, TextFileFieldFromDefinition> fromNameMap) {
        if (fieldName.equals("") || fieldName.equals("_")) return new EmptyTextFileFieldToDefinition(fieldName, null);

        String fieldToCreator = properties.getProperty(fieldName);
        TextFileFieldToCreator creator = isEmpty(fieldToCreator) ? null
                : (TextFileFieldToCreator) loadObject(rootProperties, properties, fieldToCreator);

        if (creator == null && !fromNameMap.containsKey(fieldName)) {
            throw new RuntimeException("to field definition [" + fieldName + " is unkown as an from field or converter name");
        }

        return new TextFileFieldToDefinition(fieldName, creator);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String convert(Map<String, TextFieldValue> fieldsValueMap) {
        String value = fieldsValueMap.get(fieldName).getValue();
        if (creator == null) {
            return value;
        }

        return creator.create(value, fieldsValueMap);
    }
}
