package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vo.vod.CourseVo;
import com.atguigu.ggkt.vod.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-11
 */
@Api(tags = "课程管理接口")
@RestController
@RequestMapping(value="/admin/vod/course")
//@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;


    @ApiOperation("课程最终发布")
    @PutMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable Long id){
        courseService.publishCourse(id);
        return Result.ok(null);
    }


    @ApiOperation("根据id获取课程发布信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVoById(@PathVariable Long id){
       CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);
        return Result.ok(coursePublishVo);
    }

    //根据ID获取课程信息
    @ApiOperation("根据ID获取课程信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        CourseFormVo courseFormVo = courseService.getCourseInfoById(id);
        return Result.ok(courseFormVo);
    }


    //修改课程信息
    @PostMapping("update")
    public Result update(@RequestBody CourseFormVo courseFormVo){
        courseService.updateCourseId(courseFormVo);
        return Result.ok(courseFormVo.getId());
    }


    //添加课程基本信息
    @ApiOperation("添加课程基本信息")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseFormVo){
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    @ApiOperation("课程管理接口")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        CourseQueryVo courseQueryVo){
        Page<Course> coursePage = new Page<>(page, limit);
       Map<String,Object> map = courseService.findPage(coursePage,courseQueryVo);
        return Result.ok(map);
    }

    @ApiOperation("删除课程")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourseById(id);
        return Result.ok(null);
    }

    //查询所有课程
    @GetMapping("findAll")
    public Result findAll() {
        List<Course> list = courseService.findlist();
        return Result.ok(list);
    }

}

