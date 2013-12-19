package org.n3r.aoc.check;

import org.n3r.aoc.LifeCycle;

import java.util.Collection;

/**
 * Left of checked orders.
 */
public interface Left extends LifeCycle {
    /**
     * pop up an order.
     *
     * @param keyValue order keyValue
     * @return null if no more orders are available.
     */
    Order pop(String keyValue);

    /**
     * pop up all remains orders.
     *
     * @return orders.
     */
    Collection<Order> popAll();
}
