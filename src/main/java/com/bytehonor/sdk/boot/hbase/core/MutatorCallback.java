package com.bytehonor.sdk.boot.hbase.core;

import org.apache.hadoop.hbase.client.BufferedMutator;

/**
 * mutator api的回调接口
 */
public interface MutatorCallback {

    /**
     * 使用mutator api to update put and delete
     *
     * @param mutator
     * @throws Throwable
     */
    void doInMutator(BufferedMutator mutator) throws Throwable;

}
