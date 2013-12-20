package org.n3r.aoc.check.impl.diff;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.check.DiffMode;
import org.n3r.aoc.check.DiffOut;
import org.n3r.aoc.check.Order;
import org.n3r.eql.Eql;

import java.util.List;
import java.util.Map;

public class SqlDiffOut implements DiffOut, SimpleConfigAware {
    private String configName;
    private String sqlFile;
    private String sqlid;
    private String sqlidStat;
    private AocContext aocContext;

    @Override
    public void onlyRigth(Order right) {
        logDiff(DiffMode.OnlyRight, null, right, null, null);
    }

    @Override
    public Order onlyLeft(Order left) {
        logDiff(DiffMode.OnlyLeft, left, null, null, null);

        return null;
    }

    @Override
    public void balance(Order left, Order right) {
        logDiff(DiffMode.Balance, left, right, null, null);
    }

    private void logDiff(DiffMode diffMode, Order left, Order right, String diffs, String diffsCode) {
        Eql eql = new Eql(configName).useSqlFile(sqlFile).id(sqlid);
        String batchNo = aocContext.getBatchNo();
        switch (diffMode) {
            case OnlyRight:
                eql.params(batchNo, "0R", null, right + "", null, right.keyValue(), null).execute();
                break;
            case OnlyLeft:
                eql.params(batchNo, "L0", left + "", null, null, left.keyValue(), null).execute();
                break;
            case Diff:
                eql.params(batchNo, "LR", left + "", right + "", diffs,
                        right.keyValue(), diffsCode).execute();
            case Balance:
                break;
        }
    }

    @Override
    public void endCompare(DiffStat diffStat) {
        Eql eql = new Eql(configName).useSqlFile(sqlFile).id(sqlidStat);
        eql.params(aocContext.getBatchNo(), diffStat.getOnlyLefts()
                , diffStat.getOnlyRights()
                , diffStat.getDiffs()
                , diffStat.getBalances()
                , diffStat.getStartTime()
                , diffStat.getEndTime()
                , diffStat.getTotals()
                , diffStat.getCostTime()
                , aocContext.getCheckDay()
                , aocContext.getOrderType()).execute();
    }

    @Override
    public void diff(Order left, Order right, Map<String, String> diffs, String diffsCode) {
        logDiff(DiffMode.Diff, left, right, JSON.toJSONString(diffs), diffsCode);
    }

    @Override
    public void startup(AocContext aocContext) {
        this.aocContext = aocContext;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void setSimpleConfig(String config) {
        List<String> strings = Splitter.on(',').trimResults().splitToList(config);
        // configName, SqlFile, sqlid, sqlidStat
        if (strings.size() != 4) {
            throw new RuntimeException("sql config need configName, sqlFile, sqlid and sqlidStat");
        }

        configName = strings.get(0);
        sqlFile = strings.get(1);
        sqlid = strings.get(2);
        sqlidStat = strings.get(3);
    }
}
