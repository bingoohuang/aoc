package org.n3r.aoc.file;

import org.n3r.aoc.LifeCycle;

import java.io.InputStream;

/**
 * 输入。
 */
public interface Input extends LifeCycle {
    /**
     * 从输入中读取到输入流。
     *
     * @return 输入流。
     */
    InputStream read();
}
