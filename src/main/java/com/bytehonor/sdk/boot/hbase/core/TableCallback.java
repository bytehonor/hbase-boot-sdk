package com.bytehonor.sdk.boot.hbase.core;

import org.apache.hadoop.hbase.client.Table;

/**
 * HbaseTemplate执行处理的Hbase的回调接口
 */
public interface TableCallback<T> {

    T doInTable(Table table) throws Throwable;

}
