package com.bytehonor.sdk.boot.hbase.config;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bytehonor.sdk.boot.hbase.core.HbaseTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseBootSdkAutoConfigurationTest {

    @Autowired(required = false)
    private HbaseTemplate hbaseTemplate;
    
    @Test
    public void testConfig() {
        boolean isOk = hbaseTemplate != null;
        assertTrue("*testConfig*", isOk);
    }

}
