package org.n3r.aoc.check;

/**
 * An order to be checked.
 */
public interface Order {
    /**
     * the keyValue of the order.
     * @return order keyValue.
     */
    String keyValue();

    Object fieldValue(String fieldName);
}
