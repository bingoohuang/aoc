package org.n3r.aoc.check.impl;

import com.sleepycat.je.Database;
import org.n3r.aoc.check.Left;
import org.n3r.aoc.check.Order;

import java.io.IOException;
import java.util.Collection;

public class BdbLeft implements Left {
    private Database database;

    @Override
    public Order pop(String key) {
        return null;
    }

    @Override
    public Collection<Order> popAll() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
