package org.n3r.aoc.check;

/**
 * An order to be checked.
 */
public interface Order {
    /**
     * the key of the order.
     * @return order key.
     */
    String key();

    /**
     * the detail of the order.
     * @return order detail.
     */
    <T> T detail();
}
