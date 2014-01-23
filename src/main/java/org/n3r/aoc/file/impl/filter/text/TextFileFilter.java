package org.n3r.aoc.file.impl.filter.text;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.file.Filter;
import org.n3r.aoc.file.Output;
import org.n3r.aoc.utils.Aocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class TextFileFilter implements Filter, PropertiesAware {
    private Charset charset = Charsets.UTF_8;

    private TextStatPos textStatPos = TextStatPos.none;
    private Splitter statSplitter;

    private Map<Integer, TextFileStat> statIndexMap = Maps.newHashMap(); // stat field index -> stat
    private int minStatFieldsNum = 0;

    private Map<String, TextFileFieldFromDefinition> fromNameMap = Maps.newLinkedHashMap();
    private List<TextFileFieldToDefinition> toList = Lists.newArrayList();
    private Splitter splitter;
    private int minFromFieldsNum = 0;

    Logger logger = LoggerFactory.getLogger(TextFileFilter.class);
    private AocContext aocContext;

    @Override
    public void setProperties(Properties rootProperties, Properties properties) {
        String charsetName = properties.getProperty("charset");
        if (isNotEmpty(charsetName)) charset = Charset.forName(charsetName);

        parseStatConfig(rootProperties, properties);
        parseFieldsConfig(rootProperties, properties);
    }

    private void parseFieldsConfig(Properties rootProperties, Properties properties) {
        Properties fieldsProperties = Aocs.subProperties(properties, "data.fields");
        String split = fieldsProperties.getProperty("split", ",");
        splitter = Splitter.on(split).trimResults();

        loadFieldsFromDefinition(rootProperties, fieldsProperties);
        loadFieldsToDefinition(rootProperties, fieldsProperties);
    }

    private void parseStatConfig(Properties rootProperties, Properties properties) {
        String statPos = properties.getProperty("stat.pos", "none");
        try {
            textStatPos = TextStatPos.valueOf(statPos);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(statPos + " is invalid", e);
        }

        String statSplit = properties.getProperty("stat.split", ",");
        statSplitter = Splitter.on(statSplit).trimResults();
        Properties statProperites = Aocs.subProperties(properties, "stat.fields");
        String statDefine = statProperites.getProperty("define");
        if (isEmpty(statDefine)) return;

        for (String statFieldName : Splitter.on(',').trimResults().split(statDefine)) {
            TextFileStat textFileStat = TextFileStat.createStat(rootProperties, statProperites, statFieldName);
            statIndexMap.put(minStatFieldsNum++, textFileStat);
        }
    }

    private void loadFieldsToDefinition(Properties rootProperties, Properties properties) {
        String toConfig = properties.getProperty("to");
        if (isEmpty(toConfig)) throw new RuntimeException("to config should not be empty");

        Properties fromProperties = Aocs.subProperties(properties, "to");
        for (String fieldName : Splitter.on(',').trimResults().split(toConfig)) {
            toList.add(TextFileFieldToDefinition.createField(rootProperties, fromProperties, fieldName, fromNameMap));
        }
    }

    private void loadFieldsFromDefinition(Properties rootProperties, Properties properties) {
        String fromConfig = properties.getProperty("from");
        if (isEmpty(fromConfig)) throw new RuntimeException("from config should not be empty");

        Properties fromProperties = Aocs.subProperties(properties, "from");

        for (String fieldName : Splitter.on(',').trimResults().split(fromConfig)) {
            TextFileFieldFromDefinition fieldFromDefinition = TextFileFieldFromDefinition
                    .createField(rootProperties, fromProperties, fieldName, minFromFieldsNum++);
            fromNameMap.put(fieldName, fieldFromDefinition);
        }
    }

    @Override
    public void filter(InputStream is, Output output) {
        writeFieldsName(output);

        processContent(is, output);
    }

    private void processContent(InputStream is, Output output) {
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

        if (textStatPos != TextStatPos.none) validateStat();
    }

    private void writeFieldsName(Output output) {
        List<String> toFieldNames = Lists.newArrayList();
        for (TextFileFieldToDefinition textFileFieldToDefinition : toList) {
            toFieldNames.add(textFileFieldToDefinition.getFieldName());
        }
        output.writeFieldsName(toFieldNames);
    }

    private void validateStat() {
        for (int i = 0; i < minStatFieldsNum; ++i) {
            TextFileStat textFileStat = statIndexMap.get(i);
            textFileStat.checkStat();
        }
    }


    private void parseLine(Output output, String line, int lineNo) {
        List<String> fieldsValue = splitter.splitToList(line);

        if (fieldsValue.size() < minFromFieldsNum)
            throw new RuntimeException("too small fields in line no " + lineNo + " with content " + line);

        try {
            Map<String, TextFieldValue> fieldsValueMap = Maps.newHashMap();
            for (Map.Entry<String, TextFileFieldFromDefinition> entry : fromNameMap.entrySet()) {
                TextFileFieldFromDefinition definition = entry.getValue();
                String fieldStringValue = fieldsValue.get(definition.getFieldIndex());
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

            accumuteStat(fieldsValueMap);
        } catch (Exception e) {
            logger.error("error while parsing line no {} with content {}", lineNo, line);
            throw Throwables.propagate(e);
        }
    }

    private void accumuteStat(Map<String, TextFieldValue> fieldsValue) {
        for (Map.Entry<String, TextFileFieldFromDefinition> entry : fromNameMap.entrySet()) {
            TextFileFieldFromDefinition definition = entry.getValue();
            TextFileStat textFileStat = statIndexMap.get(definition.getFieldIndex());
            if (textFileStat != null) textFileStat.accumulate(fieldsValue);
        }
    }

    private void parseStatLine(String line, int lineNo) {
        List<String> statFields = statSplitter.splitToList(line);
        if (statFields.size() < minStatFieldsNum)
            throw new RuntimeException("too small fields in line no " + lineNo + " with stat " + line);

        for (int i = 0; i < minStatFieldsNum; ++i) {
            TextFileStat textFileStat = statIndexMap.get(i);
            textFileStat.setTargetStatValue(statFields.get(i));
        }
    }

    private String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startup(AocContext aocContext) {
        this.aocContext = aocContext;
        logger.info("startup");
    }

    @Override
    public void shutdown() {
        logger.info("shutdown");
    }
}
