package com.atguigu.ggkt.live.service;


import com.atguigu.ggkt.model.live.LiveCourseConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 直播课程配置表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-26
 */
public interface LiveCourseConfigService extends IService<LiveCourseConfig> {

    LiveCourseConfig getCourseConfigCourseId(Long id);
}
