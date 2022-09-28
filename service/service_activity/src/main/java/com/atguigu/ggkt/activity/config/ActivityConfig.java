package com.atguigu.ggkt.activity.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuyusong
 * @create 2022-09-16 16:55
 */
@Configuration
@MapperScan(basePackages = "com.atguigu.ggkt.activity.mapper")
public class ActivityConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
