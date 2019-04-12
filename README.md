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