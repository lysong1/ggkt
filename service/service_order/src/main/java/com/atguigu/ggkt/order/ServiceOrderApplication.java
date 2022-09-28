package com.atguigu.ggkt.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liuyusong
 * @create 2022-09-16 15:35
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu")
public class ServiceOrderApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceOrderApplication.class, args);
        }
}