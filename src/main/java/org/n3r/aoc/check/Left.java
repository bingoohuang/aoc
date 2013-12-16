package org.n3r.aoc.check;

import java.io.Closeable;
import java.util.Collection;

/**
 * Left of checked orders.
 */
public interface Left extends Closeable {
    /**
     * pop up an order.
     * @param key order key
     * @return null if no more orders are available.
     */
    Order pop(String key);

    /**
     * pop up all remains orders.
     * @return orders.
     */
    Collection<Order> popAll();
}
