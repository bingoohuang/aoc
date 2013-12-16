package org.n3r.aoc.check;

import java.io.Closeable;

/**
 * Right side of checked orders.
 */
public interface Right extends Closeable {
    /**
     * Next order.
     * @return null if there is no more.
     */
    Order next();
}
