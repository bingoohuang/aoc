package org.n3r.aoc.file;

import org.n3r.aoc.LifeCycle;

import java.io.InputStream;

/**
 * 文件处理。
 */
public interface Filter extends LifeCycle {
    void filter(InputStream is, Output output);
}
