package org.n3r.aoc.check;

import org.n3r.aoc.LifeCycle;
import org.n3r.aoc.check.impl.diff.DiffStat;

import java.util.Map;

public interface DiffOut extends LifeCycle {
    void onlyRigth(Order right);

    Order onlyLeft(Order left);

    void balance(Order left, Order right);

    void endCompare(DiffStat diffStat);

    void diff(Order left, Order right, Map<String, String> diffs, String diffsCode);
}
