package org.n3r.aoc.check.impl.checker;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.PropertiesAware;
import org.n3r.aoc.check.*;
import org.n3r.aoc.check.impl.diff.DiffStat;
import org.n3r.aoc.utils.Aocs;
import org.n3r.aoc.utils.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.n3r.aoc.check.impl.diff.EqualsUtils.equalsTo;

public class OutsideOrderChecker extends OrderChecker implements PropertiesAware {
    private BatchNoCreator batchNoCreator;
    private Left left;
    private Right right;
    private Tuple<String, String> compareKey;
    private List<Tuple<String, String>> compareFields;
    private DiffOut diffOut;

    private DiffStat diffStat = new DiffStat();
    Logger logger = LoggerFactory.getLogger(OutsideOrderChecker.class);
    private String batchNo;

    @Override
    public void check() {
        try {
            startup(aocContext);
            compareRecords();
        } finally {
            shutdown();
        }
    }

    private void compareRecords() {
        Order rightRecord;
        while ((rightRecord = right.next()) != null) {
            String key = rightRecord.keyValue();
            Order leftRecord = left.pop(key);

            if (leftRecord == null) {
                diffOut.onlyRigth(rightRecord);
                diffStat.stat(DiffMode.OnlyRight);
            } else compareRecord(leftRecord, rightRecord);
        }

        for (Order record : left.popAll()) {
            rightRecord = diffOut.onlyLeft(record);
            if (rightRecord == null) {
                diffStat.stat(DiffMode.OnlyLeft);
            } else {
                compareRecord(record, rightRecord);
            }
        }
    }

    private void compareRecord(Order left, Order right) {
        Map<String, String> diffs = Maps.newHashMap();
        StringBuilder diffsCode = new StringBuilder(compareFields.size());

        for (Tuple<String, String> fieldName : compareFields) {
            Object leftFieldValue = left.fieldValue(fieldName._1);
            Object rightFieldValue = right.fieldValue(fieldName._2);
            if (equalsTo(leftFieldValue, rightFieldValue)) {
                diffsCode.append('0');
            } else {
                diffs.put(createDiffKey(compareKey), createDiff(leftFieldValue, rightFieldValue));
                diffsCode.append('1');
            }
        }

        if (diffs.size() <= 0) {
            diffOut.balance(left, right);
            diffStat.stat(DiffMode.Balance);
        } else {
            diffOut.diff(left, right, diffs, diffsCode.toString());
            diffStat.stat(DiffMode.Diff);
        }
    }


    private String createDiff(Object leftFieldValue, Object rightFieldValue) {
        return leftFieldValue + "-" + rightFieldValue;
    }

    private String createDiffKey(Tuple<String, String> compareField) {
        if (compareField._1.equals(compareField._2))
            return compareField._1;

        return createDiff(compareField._1, compareField._2);
    }

    private void shutdown() {
        logger.info("startup aoc with batch no {}", batchNo);

        diffStat.shutdown();
        diffOut.endCompare(diffStat);
        left.shutdown();
        right.shutdown();
    }

    private void startup(AocContext aocContext) {
        this.batchNo = batchNoCreator.createBatchNo(aocContext);
        logger.info("startup aoc with batch no {}", batchNo);

        left.startup(aocContext);
        right.startup(aocContext);
        diffStat.startup(aocContext);
        diffOut.startup(aocContext);
    }

    @Override
    public void setProperties(Properties rootProperties, Properties properties) {
        parseBatchNoCreator(rootProperties, properties);
        parseLeft(rootProperties, properties);
        parseRight(rootProperties, properties);
        pareCompareKey(rootProperties, properties);
        pareCompareFields(rootProperties, properties);
        parseDiffOutput(rootProperties, properties);
    }

    private void parseDiffOutput(Properties rootProperties, Properties properties) {
        String config = properties.getProperty("diffOut");
        if (isEmpty(config)) throw new RuntimeException("diffOut should be defined");

        this.diffOut = Aocs.loadObject(rootProperties, properties, config);
    }

    private void pareCompareFields(Properties rootProperties, Properties properties) {
        String config = properties.getProperty("compareFields");
        if (isEmpty(config)) throw new RuntimeException("compareFields should be defined");

        this.compareFields = parseTuples(config);
    }

    private void pareCompareKey(Properties rootProperties, Properties properties) {
        String config = properties.getProperty("compareKey");
        if (isEmpty(config)) throw new RuntimeException("compareKey should be defined");

        this.compareKey = parseTuple(config);
    }

    private List<Tuple<String, String>> parseTuples(String tuples) {
        List<Tuple<String, String>> tupleList = Lists.newArrayList();
        for (String str : Splitter.on(',').omitEmptyStrings().trimResults().split(tuples))
            tupleList.add(parseTuple(str));

        return tupleList;
    }


    private Tuple<String, String> parseTuple(String tuple) {
        int seperatorPos = tuple.indexOf(':');
        if (seperatorPos < 0 || seperatorPos == 0 || seperatorPos == tuple.length() - 1)
            throw new RuntimeException(tuple + " is invalid required pairs in format left:right");

        String _1 = tuple.substring(0, seperatorPos);
        String _2 = tuple.substring(seperatorPos + 1);

        return Tuple.make(_1, _2);
    }

    private void parseRight(Properties rootProperties, Properties properties) {
        String config = properties.getProperty("right");
        if (isEmpty(config)) throw new RuntimeException("right should be defined");

        this.right = Aocs.loadObject(rootProperties, properties, config);
    }

    private void parseLeft(Properties rootProperties, Properties properties) {
        String config = properties.getProperty("left");
        if (isEmpty(config)) throw new RuntimeException("left should be defined");

        this.left = Aocs.loadObject(rootProperties, properties, config);
    }

    private void parseBatchNoCreator(Properties rootProperties, Properties properties) {
        String batchNoCreatorName = properties.getProperty("batchNoCreator",
                CurrentTimeMillisBatchNoCreator.class.getName());

        batchNoCreator = Aocs.loadObject(rootProperties, properties, batchNoCreatorName);
    }
}
