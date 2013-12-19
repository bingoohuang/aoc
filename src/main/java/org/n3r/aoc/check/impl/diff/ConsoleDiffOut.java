package org.n3r.aoc.check.impl.diff;

import org.n3r.aoc.AocContext;
import org.n3r.aoc.check.DiffOut;
import org.n3r.aoc.check.Order;

import java.util.Map;

public class ConsoleDiffOut implements DiffOut {
    private AocContext aocContext;

    @Override
    public void onlyRigth(Order right) {
        System.out.println("only right:" + right);
    }

    @Override
    public Order onlyLeft(Order left) {
        System.out.println("only left:" + left);

        return null;
    }

    @Override
    public void balance(Order left, Order right) {
        System.out.println("balance, left:" +  left + ", right:" + right);
    }

    @Override
    public void endCompare(DiffStat diffStat) {
        System.out.print("end comparing with ");
        System.out.println(diffStat);
    }

    @Override
    public void diff(Order left, Order right, Map<String, String> diffs, String diffsCode) {
        System.out.println("diffs:" + diffs);
        System.out.println("diffsCode:" + diffsCode);
        System.out.println("left:" + left);
        System.out.println("right:" + right);
    }

    @Override
    public void startup(AocContext aocContext) {
        this.aocContext = aocContext;
        System.out.println("startup comparing with context " + aocContext);
    }

    @Override
    public void shutdown() {
        System.out.println("shutdown comparing with context " + aocContext);
    }
}
