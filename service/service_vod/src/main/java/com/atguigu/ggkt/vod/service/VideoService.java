package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-11
 */
public interface VideoService extends IService<Video> {

    void removeVideoByCourseId(Long id);


    void removeVideoById(Long id);
}
