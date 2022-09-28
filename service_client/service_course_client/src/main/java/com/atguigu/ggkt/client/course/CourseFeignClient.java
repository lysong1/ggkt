package com.atguigu.ggkt.client.course;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author liuyusong
 * @create 2022-09-19 22:42
 */
@FeignClient(value = "service-vod")
public interface CourseFeignClient {

    @ApiOperation("根据关键字查询课程")
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    List<Course> findByKeyword(@PathVariable String keyword);

    @ApiOperation("根据ID查询课程")
    @GetMapping("/api/vod/course/inner/getById/{courseId}")
    Course getById(@PathVariable Long courseId);


    @ApiOperation(value = "获取")
    @GetMapping("/admin/vod/teacher/inner/get/{id}")
    public Teacher getTeacherInfo(@PathVariable("id") Long id);
}
