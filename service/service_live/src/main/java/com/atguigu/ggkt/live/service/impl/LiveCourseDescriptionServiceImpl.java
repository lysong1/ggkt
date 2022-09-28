package com.atguigu.ggkt.live.service.impl;


import com.atguigu.ggkt.live.mapper.LiveCourseDescriptionMapper;
import com.atguigu.ggkt.live.service.LiveCourseDescriptionService;
import com.atguigu.ggkt.model.live.LiveCourseDescription;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-26
 */
@Service
public class LiveCourseDescriptionServiceImpl extends ServiceImpl<LiveCourseDescriptionMapper, LiveCourseDescription> implements LiveCourseDescriptionService {

    @Override
    public LiveCourseDescription getLiveCourseById(Long id) {
        LambdaQueryWrapper<LiveCourseDescription> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(LiveCourseDescription::getLiveCourseId,id);
        LiveCourseDescription liveCourseDescription = baseMapper.selectOne(wrapper);

        return liveCourseDescription;
    }
}
