package org.n3r.aoc;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 对账上下文。
 */
public class AocContext {
    private Map<String, String> context = Maps.newHashMap();

    // checked day, with format "yyyyMMdd".
    public static final String CHECK_DAY = "checkDay";
    // checked order type.
    public static final String ORDER_TYPE = "orderType";
    // batch no.
    public static final String BATCH_NO = "batchNo";

    public void put(String contextKey, String contextValue) {
        context.put(contextKey, contextValue);
    }

    public void setBatchNo(String batchNo) {
        context.put(BATCH_NO, batchNo);
    }

    public String getBatchNo() {
        return context.get(BATCH_NO);
    }

    public String getCheckDay() {
        return context.get(CHECK_DAY);
    }

    public void setCheckDay(String checkDay) {
        context.put(CHECK_DAY, checkDay);
    }

    public String getOrderType() {
        return context.get(ORDER_TYPE);
    }

    public void setOrderType(String orderType) {
        context.put(ORDER_TYPE, orderType);
    }

    public Map<String, String> getAocContext() {
        return context;
    }

    @Override
    public String toString() {
        return context.toString();
    }
}
