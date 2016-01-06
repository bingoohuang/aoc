package org.n3r.aoc.check;

import org.n3r.aoc.LifeCycle;
import org.n3r.aoc.check.impl.diff.DiffStat;

import java.util.Map;

/**
 * 差异输出。
 */
public interface DiffOut extends LifeCycle {
    /**
     * 左右对账对平。
     *
     * @param left  左订单
     * @param right 右订单
     */
    void balance(Order left, Order right);

    /**
     * 只有右边有，左边找不到对应订单。
     *
     * @param right 右订单
     */
    void onlyRigth(Order right);

    /**
     * 只有左边有，右边无。
     *
     * @param left 左订单
     * @return null表示右边确实没有，非null表示右边重新找到该订单
     */
    Order onlyLeft(Order left);

    /**
     * 左右订单都存在，但是对账未平。
     *
     * @param left      左订单
     * @param right     右订单
     * @param diffs     差异字段(key为"左字段名-右字段名"，value为"左字段取值-右字段取值")
     * @param diffsCode 差异码。多少位表示比较了多少字段。0表示字段取值相同，1表示不相同。
     */
    void diff(Order left, Order right, Map<String, String> diffs, String diffsCode);

    /**
     * 比对结束。
     *
     * @param diffStat 差异统计信息
     */
    void endCompare(DiffStat diffStat);
}
