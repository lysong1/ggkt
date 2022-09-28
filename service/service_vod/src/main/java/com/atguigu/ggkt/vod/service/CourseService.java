package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-11
 */
public interface CourseService extends IService<Course> {

    Map<String, Object> findPage(Page<Course> coursePage, CourseQueryVo courseQueryVo);

    Long saveCourseInfo(CourseFormVo courseFormVo);


    CourseFormVo getCourseInfoById(Long id);

    void updateCourseId(CourseFormVo courseFormVo);

    CoursePublishVo getCoursePublishVoById(Long id);

    void publishCourse(Long id);

    void removeCourseById(Long id);

    Map<String, Object> getInfoById(Long courseId);

    List<Course> findlist();

}
