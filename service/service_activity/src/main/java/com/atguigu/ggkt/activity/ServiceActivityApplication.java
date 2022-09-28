package com.atguigu.ggkt.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liuyusong
 * @create 2022-09-16 16:53
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu")
public class ServiceActivityApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceActivityApplication.class, args);
        }
}
