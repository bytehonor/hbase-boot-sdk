package com.bytehonor.sdk.boot.hbase.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import com.bytehonor.sdk.boot.hbase.config.HbaseProperties;
import com.bytehonor.sdk.boot.hbase.error.HbaseSdkException;

/**
 * nubia框架Hbase的操作模板类
 *
 */
public class HbaseTemplate implements HbaseOperations {

    private static final Logger LOG = LoggerFactory.getLogger(HbaseTemplate.class);

    private HbaseProperties hbaseProperties;

    private Configuration configuration;

    private volatile Connection connection;

    public HbaseTemplate(Configuration configuration, HbaseProperties hbaseProperties) {
        Assert.notNull(configuration, "configuration can not be null！");
        Assert.notNull(hbaseProperties, "hbaseProperties can not be null！");
        this.configuration = configuration;
        this.hbaseProperties = hbaseProperties;
        this.initConnection();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public HbaseProperties getHbaseProperties() {
        return hbaseProperties;
    }

    public void setHbaseProperties(HbaseProperties hbaseProperties) {
        this.hbaseProperties = hbaseProperties;
    }

    private void initConnection() {
        try {
            ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(hbaseProperties.getConnCorePoolSize(),
                    hbaseProperties.getConnMaxPoolSize(), 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
            poolExecutor.prestartCoreThread();
            this.connection = ConnectionFactory.createConnection(configuration, poolExecutor);
            LOG.info("HbaseTemplate initConnection ok");
        } catch (IOException e) {
            LOG.error("hbase initConnection failed", e);
        }
    }

    public Connection getConnection() {
        if (null == this.connection) {
            synchronized (this) {
                if (null == this.connection) {
                    this.initConnection();
                }
            }
        }
        return this.connection;
    }

    @Override
    public <T> T execute(String tableName, TableCallback<T> mapper) {
        Assert.notNull(mapper, "Callback object must not be null");
        Assert.notNull(tableName, "No table specified");

        StopWatch sw = new StopWatch();
        sw.start();
        Table table = null;
        try {
            table = this.getConnection().getTable(TableName.valueOf(tableName));
            return mapper.doInTable(table);
        } catch (Throwable throwable) {
            throw new HbaseSdkException(throwable);
        } finally {
            if (null != table) {
                try {
                    table.close();
                    sw.stop();
                } catch (IOException e) {
                    LOG.error("hbase close failed", e);
                }
            }
        }
    }

    @Override
    public <T> List<T> find(String tableName, String family, RowMapper<T> mapper) {
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));
        return this.find(tableName, scan, mapper);
    }

    @Override
    public <T> List<T> find(String tableName, String family, String qualifier, RowMapper<T> mapper) {
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        return this.find(tableName, scan, mapper);
    }

    @Override
    public <T> List<T> find(String tableName, Scan scan, RowMapper<T> mapper) {
        return this.execute(tableName, new TableCallback<List<T>>() {

            @Override
            public List<T> doInTable(Table table) throws Throwable {
                ResultScanner scanner = table.getScanner(scan);
                try {
                    List<T> rs = new ArrayList<>();
                    int rowNum = 0;
                    for (Result result : scanner) {
                        rs.add(mapper.mapRow(result, rowNum++));
                    }
                    return rs;
                } finally {
                    scanner.close();
                }
            }
        });
    }

    @Override
    public <T> T get(String tableName, String rowName, RowMapper<T> mapper) {
        return this.get(tableName, rowName, null, null, mapper);
    }

    @Override
    public <T> T get(String tableName, String rowName, String familyName, RowMapper<T> mapper) {
        return this.get(tableName, rowName, familyName, null, mapper);
    }

    @Override
    public <T> T get(String tableName, String rowName, String familyName, String qualifier, RowMapper<T> mapper) {
        return this.execute(tableName, new TableCallback<T>() {
            @Override
            public T doInTable(Table table) throws Throwable {
                Get get = new Get(Bytes.toBytes(rowName));
                if (StringUtils.isNotBlank(familyName)) {
                    byte[] family = Bytes.toBytes(familyName);
                    if (StringUtils.isNotBlank(qualifier)) {
                        get.addColumn(family, Bytes.toBytes(qualifier));
                    } else {
                        get.addFamily(family);
                    }
                }
                Result result = table.get(get);
                return mapper.mapRow(result, 0);
            }
        });
    }

    @Override
    public boolean put(String tableName, String rowName, String familyName, String qualifier, byte[] data) {
        Assert.hasLength(rowName, "No rowName specified");
        Assert.hasLength(familyName, "No familyName specified");
        Assert.hasLength(qualifier, "No qualifier specified");
        Assert.notNull(data, "data can not be null");

        return execute(tableName, new TableCallback<Boolean>() {
            @Override
            public Boolean doInTable(Table htable) throws Throwable {
                boolean isDone = false;
                Put put = new Put(Bytes.toBytes(rowName)).addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier),
                        data);
                htable.put(put);
                isDone = true;
                return isDone;
            }
        });
    }

    @Override
    public boolean delete(String tableName, String rowName, String familyName) {
        return delete(tableName, rowName, familyName, null);
    }

    @Override
    public boolean delete(String tableName, String rowName, String familyName, String qualifier) {
        Assert.hasLength(rowName, "No familyName specified");
        Assert.hasLength(familyName, "No familyName specified");
        return execute(tableName, new TableCallback<Boolean>() {
            @Override
            public Boolean doInTable(Table htable) throws Throwable {
                boolean isDone = false;
                Delete delete = new Delete(Bytes.toBytes(rowName));
                byte[] family = Bytes.toBytes(familyName);

                if (qualifier != null) {
                    delete.addColumn(family, Bytes.toBytes(qualifier));
                } else {
                    delete.addFamily(family);
                }

                htable.delete(delete);
                isDone = true;
                return isDone;
            }
        });
    }
}
