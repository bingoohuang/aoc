package org.n3r.aoc.check;

import com.google.common.collect.Maps;

import java.util.Map;

public class OrderCheckContext {
    private Map<String, String> context = Maps.newHashMap();

    public static final String CHECK_DAY = "checkDay";
    public static final String ORDER_TYPE = "orderType";

    private String checkDay;   // checked day, with format "yyyyMMdd".
    private String orderType;  // checked order type.

    public void put(String contextKey, String contextValue) {
        context.put(contextKey, contextValue);
    }

    public String getCheckDay() {
        return checkDay;
    }

    public void setCheckDay(String checkDay) {
        this.checkDay = checkDay;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Map<String, String> getContext() {
        Map<String, String> returnContext = Maps.newHashMap(context);

        returnContext.put(CHECK_DAY, checkDay);
        returnContext.put(ORDER_TYPE, orderType);

        return returnContext;
    }
}
