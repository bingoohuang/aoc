package org.n3r.aoc.check;

import org.n3r.aoc.LifeCycle;

/**
 * Right side of checked orders.
 */
public interface Right extends LifeCycle {
    /**
     * Next order.
     *
     * @return null if there is no more.
     */
    Order next();
}
