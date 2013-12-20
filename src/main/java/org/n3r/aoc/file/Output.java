package org.n3r.aoc.file;

import org.n3r.aoc.LifeCycle;

import java.util.List;

/**
 * 输出。
 */
public interface Output extends LifeCycle {
    /**
     * 输出字段名列表。
     *
     * @param toFieldNames 字段名列表
     */
    void writeFieldsName(List<String> toFieldNames);

    /**
     * 输出字段取值。
     *
     * @param toFieldsValue
     */
    void write(List<String> toFieldsValue);
}
