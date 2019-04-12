package com.bytehonor.sdk.boot.hbase.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.bytehonor.sdk.boot.hbase.core.HbaseTemplate;

/**
 * hbase自动配置类
 *
 */
@Configuration
@EnableConfigurationProperties(HbaseProperties.class)
@ConditionalOnClass(HbaseTemplate.class)
public class HbaseAutoConfiguration {

    @Autowired
    private HbaseProperties hbaseProperties;

    @Bean
    @ConditionalOnMissingBean(HbaseTemplate.class)
    public HbaseTemplate hbaseTemplate() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set(HConstants.ZOOKEEPER_QUORUM, hbaseProperties.getZkQuorum());
        configuration.setInt(HConstants.HBASE_CLIENT_SCANNER_CACHING, hbaseProperties.getScannerCaching());

        if (!StringUtils.isEmpty(hbaseProperties.getZkClientPort())) {
            configuration.set(HConstants.ZOOKEEPER_CLIENT_PORT, hbaseProperties.getZkClientPort());
        }

        return new HbaseTemplate(configuration, hbaseProperties);
    }

}
