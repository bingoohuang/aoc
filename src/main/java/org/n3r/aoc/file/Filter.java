package org.n3r.aoc.file;

import org.n3r.aoc.LifeCycle;

import java.io.InputStream;

public interface Filter extends LifeCycle {
    void filter(InputStream is, Output output);
}
