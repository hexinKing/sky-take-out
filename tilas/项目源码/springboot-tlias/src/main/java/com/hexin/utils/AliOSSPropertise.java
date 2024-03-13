package com.hexin.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 工具类
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
//@ConfigurationProperties(prefix ="aliyun.oss")：适合批量添加配置
public class AliOSSPropertise {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;


}
