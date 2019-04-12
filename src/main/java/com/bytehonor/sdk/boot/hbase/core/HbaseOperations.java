package com.bytehonor.sdk.boot.hbase.core;

import org.apache.hadoop.hbase.client.Scan;

import java.util.List;

/**
 * Hbase的操作接口
 *
 */
public interface HbaseOperations {

    /**
     * 执行回调的方法
     *
     * @param tableName
     * @param mapper
     * @param <T>
     * @return
     */
    <T> T execute(String tableName, TableCallback<T> mapper);

    /**
     * 查找指定表中的列簇信息
     *
     * @param tableName
     * @param family
     * @param mapper
     * @param <T>
     * @return
     */
    <T> List<T> find(String tableName, String family, final RowMapper<T> mapper);

    /**
     * 浏览指定表的各列信息
     *
     * @param tableName
     * @param family
     * @param qualifier
     * @param mapper
     * @param <T>
     * @return
     */
    <T> List<T> find(String tableName, String family, String qualifier, final RowMapper<T> mapper);

    /**
     * 根据Scan查找表信息
     *
     * @param tableName
     * @param scan
     * @param mapper
     * @param <T>
     * @return
     */
    <T> List<T> find(String tableName, final Scan scan, final RowMapper<T> mapper);

    /**
     * 获取指定表中指定行的信息
     *
     * @param tableName
     * @param rowName
     * @param mapper
     * @param <T>
     * @return
     */
    <T> T get(String tableName, String rowName, final RowMapper<T> mapper);

    /**
     * 获取指定表中指定行对应的列簇信息
     *
     * @param tableName
     * @param rowName
     * @param familyName
     * @param mapper
     * @param <T>
     * @return
     */
    <T> T get(String tableName, String rowName, String familyName, final RowMapper<T> mapper);

    /**
     * 获取指定表中指定行，指定列簇的指定列信息
     *
     * @param tableName
     * @param rowName
     * @param familyName
     * @param qualifier
     * @param mapper
     * @param <T>
     * @return
     */
    <T> T get(String tableName, final String rowName, final String familyName, final String qualifier, final RowMapper<T> mapper);

    /**
     * put单个值到指定的表
     *
     * @param tableName
     * @param rowName
     * @param familyName
     * @param qualifier
     * @param data
     */
    boolean put(String tableName, final String rowName, final String familyName, final String qualifier, final byte[] data);

    /**
     * 从指定表和列family中删除单个qualifier
     *
     * @param tableName
     * @param rowName
     * @param familyName
     */
    boolean delete(String tableName, final String rowName, final String familyName);

    /**
     * 从指定表中删除单个cell
     *
     * @param tableName
     * @param rowName
     * @param familyName
     * @param qualifier
     */
    boolean delete(String tableName, final String rowName, final String familyName, final String qualifier);

}

