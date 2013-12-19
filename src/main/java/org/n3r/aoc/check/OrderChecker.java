package org.n3r.aoc.check;

import org.apache.commons.lang3.StringUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.utils.Aocs;

import java.util.Properties;

public abstract class OrderChecker {
    protected AocContext aocContext = new AocContext();

    public abstract void check();


    public static OrderChecker fromProperties(Properties properties) {
        String model = properties.getProperty("model");
        if (StringUtils.isEmpty(model)) {
            throw new RuntimeException("model is not defined in properties.");
        }

        return Aocs.loadObject(properties, properties, model);
    }

    public OrderChecker aocContext(AocContext aocContext) {
        this.aocContext = aocContext;
        return this;
    }
}
