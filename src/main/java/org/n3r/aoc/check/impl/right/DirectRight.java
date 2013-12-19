package org.n3r.aoc.check.impl.right;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.check.Order;
import org.n3r.aoc.check.Right;
import org.n3r.aoc.check.impl.order.RecordOrder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class DirectRight implements Right, SimpleConfigAware {
    private String directInput;
    private BufferedReader reader;
    private String[] fieldsName;

    @Override
    public Order next() {
        try {
            String valueLine = reader.readLine();
            if (valueLine == null) return null;
            String[] fieldsValue = Iterables.toArray(Splitter.on(',').split(valueLine), String.class);

            return new RecordOrder(fieldsValue, fieldsName, fieldsValue[0]);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startup(AocContext aocContext) {
        try {
            reader = new BufferedReader(new StringReader(directInput));
            String headLine = reader.readLine();
            fieldsName = Iterables.toArray(Splitter.on(',').split(headLine), String.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        IOUtils.closeQuietly(reader);
    }

    @Override
    public void setSimpleConfig(String config) {
        this.directInput = config;
    }
}
