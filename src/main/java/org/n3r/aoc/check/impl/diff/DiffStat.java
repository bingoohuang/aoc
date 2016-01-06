package org.n3r.aoc.check.impl.diff;

import org.n3r.aoc.AocContext;
import org.n3r.aoc.LifeCycle;
import org.n3r.aoc.check.DiffMode;

import java.sql.Timestamp;

/**
 * Statistics for Difference.
 */
public class DiffStat implements LifeCycle {
    private long start;
    private int onlyRights;
    private int balances;
    private int diffs;
    private int onlyLefts;
    private long end;
    private int totals;
    private AocContext aocContext;
    private Object costTime;

    @Override
    public void startup(AocContext aocContext) {
        this.start = System.currentTimeMillis();
        this.aocContext = aocContext;
    }

    public void stat(DiffMode diffMode) {
        ++totals;
        switch (diffMode) {
            case Balance:
                ++this.balances;
                break;
            case Diff:
                ++this.diffs;
                break;
            case OnlyLeft:
                ++this.onlyLefts;
                break;
            case OnlyRight:
                ++this.onlyRights;
                break;
        }
    }

    @Override
    public void shutdown() {
        this.end = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "DiffStat{" +
                "start=" + start +
                ", onlyRights=" + onlyRights +
                ", balances=" + balances +
                ", diffs=" + diffs +
                ", onlyLefts=" + onlyLefts +
                ", end=" + end +
                ", aocContext='" + aocContext + '\'' +
                '}';
    }

    public int getOnlyLefts() {
        return onlyLefts;
    }

    public int getOnlyRights() {
        return onlyRights;
    }

    public int getDiffs() {
        return diffs;
    }

    public int getBalances() {
        return balances;
    }

    public Timestamp getStartTime() {
        return new Timestamp(start);
    }

    public Timestamp getEndTime() {
        return new Timestamp(end);
    }

    public Object getTotals() {
        return totals;
    }

    public int getCostTime() {
        return (int) ((end - start) / 1000);
    }
}
