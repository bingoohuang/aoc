package org.n3r.aoc.file.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.ConfigLoadable;
import org.n3r.aoc.file.Filter;
import org.n3r.aoc.file.Output;
import org.n3r.aoc.file.TextFieldValue;
import org.n3r.aoc.utils.Aocs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TextFileFilter implements Filter, ConfigLoadable {
    private Charset charset = Charsets.UTF_8;
    private TextStatPos textStatPos = TextStatPos.none;
    private String split = ",";
    private Map<String, TextFileFieldFromDefinition> fromNameMap = Maps.newHashMap();
    private Map<Integer, TextFileFieldFromDefinition> fromIndexMap = Maps.newHashMap();
    private List<TextFileFieldToDefinition> toList = Lists.newArrayList();
    private Splitter splitter;
    private int leastFromFieldsNum = 0;

    @Override
    public void loadConfig(Properties properties) {
        String charsetName = properties.getProperty("charset");
        if (StringUtils.isNotEmpty(charsetName)) {
            charset = Charset.forName(charsetName);
        }

        String statPos = properties.getProperty("stat.pos", "none");
        try {
            textStatPos = TextStatPos.valueOf(statPos);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(statPos + " is invalid", e);
        }

        Properties fieldsProperties = Aocs.subProperties(properties, "data.fields");
        split = properties.getProperty("split", ",");
        splitter = Splitter.on(split).trimResults();

        loadFieldsFromDefinition(fieldsProperties);
        loadFieldsToDefinitiion(fieldsProperties);
    }

    private void loadFieldsToDefinitiion(Properties properties) {
        String toConfig = properties.getProperty("to");
        if (StringUtils.isEmpty(toConfig)) {
            throw new RuntimeException("to config should not be empty");
        }

        Properties fromProperties = Aocs.subProperties(properties, "to");
        for (String fieldName : Splitter.on(split).trimResults().split(toConfig)) {
            toList.add(TextFileFieldToDefinition.createField(fromProperties, fieldName, fromNameMap));
        }
    }

    private void loadFieldsFromDefinition(Properties properties) {
        String fromConfig = properties.getProperty("from");
        if (StringUtils.isEmpty(fromConfig)) {
            throw new RuntimeException("from config should not be empty");
        }

        Properties fromProperties = Aocs.subProperties(properties, "from");

        for (String fieldName : Splitter.on(split).trimResults().split(fromConfig)) {
            TextFileFieldFromDefinition fieldFromDefinition = TextFileFieldFromDefinition
                    .createField(fromProperties, fieldName);
            fromNameMap.put(fieldName, fieldFromDefinition);
            fromIndexMap.put(leastFromFieldsNum++, fieldFromDefinition);
        }
    }

    @Override
    public void filter(InputStream is, Output output) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        String line, lastLine = null;
        int lineNo = 1;

        for (; (line = readLine(reader)) != null; ++lineNo) {
            if (lineNo == 1 && textStatPos == TextStatPos.first) {
                parseStatLine(line, lineNo);
                continue;
            }

            if (lastLine != null) parseLine(output, lastLine, lineNo - 1);
            lastLine = line;
        }

        if (lastLine != null) {
            if (textStatPos == TextStatPos.last) parseStatLine(lastLine, lineNo);
            else parseLine(output, lastLine, lineNo - 1);
        }
    }

    private void parseLine(Output output, String line, int lineNo) {
        List<String> fieldsValue = splitter.splitToList(line);

        if (fieldsValue.size() < leastFromFieldsNum) {
            throw new RuntimeException("too small fields in line no " + lineNo + " with contemt " + line);
        }

        Map<String, TextFieldValue> fieldsValueMap = Maps.newHashMap();
        for (int i = 0; i < leastFromFieldsNum; ++i) {
            TextFileFieldFromDefinition definition = fromIndexMap.get(i);
            String fieldStringValue = fieldsValue.get(i);
            TextFieldValue value = definition.createValue(fieldStringValue);
            value.validate();
            fieldsValueMap.put(definition.getFieldName(), value);
        }

        List<String> toFieldsValue = Lists.newArrayList();
        for (TextFileFieldToDefinition toDefinition : toList) {
            String value = toDefinition.convert(fieldsValueMap);
            toFieldsValue.add(value);
        }

        output.write(toFieldsValue);
    }

    private void parseStatLine(String line, int lineNo) {

    }

    private String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }
}
