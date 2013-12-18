package org.n3r.aoc.file;

import org.n3r.aoc.AocContext;
import org.n3r.aoc.LifeCycle;

import java.util.List;

public interface Output extends LifeCycle {
    void write(AocContext aocContext, List<String> toFieldsValue);

    void writeFieldsName(List<String> toFieldNames);
}
