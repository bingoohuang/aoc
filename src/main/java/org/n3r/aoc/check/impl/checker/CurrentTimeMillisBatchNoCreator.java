package org.n3r.aoc.check.impl.checker;

import org.n3r.aoc.AocContext;
import org.n3r.aoc.check.BatchNoCreator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimeMillisBatchNoCreator implements BatchNoCreator {
    @Override
    public String createBatchNo(AocContext aocContext) {
        String batchNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        aocContext.put("batchNo", batchNo);
        return batchNo;
    }
}
