# hbase-boot-sdk
hbase-boot-sdk

## metadata
```
dependencies {
    annotationProcessor "org.springframework.boot:spring-boot-configuration-processor"
}

If you are using an additional-spring-configuration-metadata.json file, the compileJava task should be configured to depend on the processResourcestask, as shown in the following example:

compileJava.dependsOn(processResources)
```

## properties
```
hbase.boot.zk-quorum=10.206.19.110,10.206.19.111,10.206.19.112
hbase.boot.scanner-caching=2000
hbase.boot.conn-core-pool-size=100
hbase.boot.conn-max-pool-size=200
```