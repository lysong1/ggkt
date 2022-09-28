package com.atguigu.ggkt.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liuyusong
 * @create 2022-09-26 16:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "mtcloud")
public class MTCloudAccountConfig {

    private String openId;
    private String openToken;

}
