package org.n3r.aoc.file;

import org.n3r.aoc.LifeCycle;

import java.util.List;

public interface Output extends LifeCycle {
    void write(List<String> toFieldsValue);
}
