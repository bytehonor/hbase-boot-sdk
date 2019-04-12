package com.bytehonor.sdk.boot.hbase.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hbase.boot")
public class HbaseProperties {

    /**
     * Hbase使用的zookeeper集群的URL配置，多个host中间用逗号（,）分割，默认值：localhost
     */
    private String zkQuorum = "localhost";

    /**
     * Hbase使用的zookeeper集群的clientPort
     */
    private String zkClientPort;

    /**
     * Hbase的表scanner的caching大小，默认为5000
     */
    private int scannerCaching = 5000;

    /**
     * Hbase连接池的最小线程数，默认为10
     */
    private int connCorePoolSize = 10;

    /**
     * Hbase连接池的最大线程数，默认为Integer.MAX_VALUE
     */
    private int connMaxPoolSize = Integer.MAX_VALUE;

    public String getZkQuorum() {
        return zkQuorum;
    }

    public void setZkQuorum(String zkQuorum) {
        this.zkQuorum = zkQuorum;
    }

    public String getZkClientPort() {
        return zkClientPort;
    }

    public void setZkClientPort(String zkClientPort) {
        this.zkClientPort = zkClientPort;
    }

    public int getScannerCaching() {
        return scannerCaching;
    }

    public void setScannerCaching(int scannerCaching) {
        this.scannerCaching = scannerCaching;
    }

    public int getConnCorePoolSize() {
        return connCorePoolSize;
    }

    public void setConnCorePoolSize(int connCorePoolSize) {
        this.connCorePoolSize = connCorePoolSize;
    }

    public int getConnMaxPoolSize() {
        return connMaxPoolSize;
    }

    public void setConnMaxPoolSize(int connMaxPoolSize) {
        this.connMaxPoolSize = connMaxPoolSize;
    }

}
