package com.atguigu.ggkt.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liuyusong
 * @create 2022-08-30 14:44
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
@EnableDiscoveryClient
public class ServiceVodApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceVodApplication.class, args);
        }
}
