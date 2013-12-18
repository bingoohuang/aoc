package org.n3r.aoc;

public interface LifeCycle {
    void startup(AocContext aocContext);
    void shutdown();
}
