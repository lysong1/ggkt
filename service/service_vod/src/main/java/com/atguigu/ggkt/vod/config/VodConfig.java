package com.atguigu.ggkt.vod.config;

/**
 * @author liuyusong
 * @create 2022-08-30 14:45
 */

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan("com.atguigu.ggkt.vod.mapper")
public class VodConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
