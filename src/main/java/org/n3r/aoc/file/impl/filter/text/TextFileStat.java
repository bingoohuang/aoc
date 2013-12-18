package org.n3r.aoc.file.impl.filter.text;

import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.utils.Aocs;

import java.util.Map;
import java.util.Properties;

public class TextFileStat {
    private final String statFieldName;
    private final TextFileStatValidator accumulator;

    public TextFileStat(String statFieldName, TextFileStatValidator accumulator) {
        this.statFieldName = statFieldName;
        this.accumulator = accumulator;
    }

    public static TextFileStat createStat(Properties rootProperties, Properties statProperites, String statFieldName) {
        if (statFieldName.equals("") || statFieldName.equals("_"))
            return new TextFileStat(statFieldName, null);

        String statAccumulatorName = statProperites.getProperty(statFieldName);
        if (StringUtils.isEmpty(statAccumulatorName))
            throw new RuntimeException("stat field [" + statFieldName + "] should define its accumulator");

        TextFileStatValidator accumulator = Aocs.loadObject(rootProperties, statProperites, statAccumulatorName);
        return new TextFileStat(statFieldName, accumulator);
    }

    public void setTargetStatValue(String targetStatValue) {
        if (accumulator == null) return;
        accumulator.setTargetStatValue(targetStatValue);
    }

    public void checkStat() {
        if (accumulator == null) return;
        accumulator.checkStat();
    }

    public void accumulate(Map<String, TextFieldValue> fieldValue) {
        if (accumulator == null) return;
        accumulator.accumulate(fieldValue);
    }
}
