package com.bytehonor.sdk.boot.hbase.core;

import org.apache.hadoop.hbase.client.Result;

/**
 * 表行数据的mapping回调处理
 */
public interface RowMapper<T> {

    T mapRow(Result result, int rowNum) throws Exception;

}