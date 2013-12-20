package org.n3r.aoc;

/**
 * 生命周期。
 */
public interface LifeCycle {
    /**
     * 启动。
     * @param aocContext 上下文。
     */
    void startup(AocContext aocContext);

    /**
     * 结束。
     */
    void shutdown();
}
