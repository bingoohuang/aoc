package org.n3r.aoc.check.impl.right;

import com.google.common.base.Splitter;
import org.n3r.aoc.AocContext;
import org.n3r.aoc.SimpleConfigAware;
import org.n3r.aoc.check.Order;
import org.n3r.aoc.check.Right;
import org.n3r.aoc.utils.Aocs;
import org.n3r.eql.Eql;
import org.n3r.eql.map.EqlRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SqlRight implements Right, SimpleConfigAware, EqlRowMapper {
    private String configName;
    private String sqlFile;

    private BlockingQueue<Order> queue = new ArrayBlockingQueue<Order>(10000, true);
    private String sqlid;

    @Override
    public Order next() {
        Order record = takeFromQueue();
        return (record == StopOrder.instance) ? null : record;
    }

    @Override
    public void startup(final AocContext aocContext) {
        final EqlRowMapper mapper = this;
        new Thread() {
            @Override
            public void run() {
                new Eql(configName).useSqlFile(sqlFile).id(sqlid).params(aocContext)
                        .returnType(mapper).setFetchSize(10000).execute();
                putToQueue(StopOrder.instance);
            }
        }.start();
    }


    private void putToQueue(Order record) {
        try {
            queue.put(record);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Order takeFromQueue() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void setSimpleConfig(String config) {
        List<String> strings = Splitter.on(',').trimResults().splitToList(config);
        // configName, SqlFile, sqlid
        if (strings.size() != 3) {
            throw new RuntimeException("sql config need configName, sqlFile and sqlid");
        }

        configName = strings.get(0);
        sqlFile = strings.get(1);
        sqlid = strings.get(2);
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        putToQueue(Aocs.mapRow(rs, rowNum));
        return null;
    }
}
