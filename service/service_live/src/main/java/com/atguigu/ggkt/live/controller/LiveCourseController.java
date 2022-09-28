package com.atguigu.ggkt.live.controller;


import com.atguigu.ggkt.live.service.LiveCourseAccountService;
import com.atguigu.ggkt.live.service.LiveCourseConfigService;
import com.atguigu.ggkt.live.service.LiveCourseGoodsService;
import com.atguigu.ggkt.live.service.LiveCourseService;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.model.live.LiveCourseAccount;
import com.atguigu.ggkt.model.live.LiveCourseConfig;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-26
 */
@RestController
@RequestMapping(value="/admin/live/liveCourse")
public class LiveCourseController {

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @Autowired
    private LiveCourseService liveCourseService;

    @ApiOperation(value = "获取最近的直播")
    @GetMapping("findLatelyList")
    public Result findLatelyList() {
           List<LiveCourseVo> list = liveCourseService.findLatelyList();
        return Result.ok(list);
    }


    @ApiOperation(value = "修改配置")
    @PutMapping("updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "获取")
    @GetMapping("getCourseConfig/{id}")
    public Result getCourseConfig(@PathVariable Long id) {
       LiveCourseConfigVo liveCourseConfigVo = liveCourseService.getCoucrseConfig(id);
        return Result.ok(liveCourseConfigVo);
    }

    @ApiOperation(value = "获取")
    @GetMapping("getLiveCourseAccount/{id}")
    public Result getLiveCourseAccount(@PathVariable Long id){
       LiveCourseAccount liveCourseAccount = liveCourseAccountService.getLiveCourseAccountCourseId(id);
        return Result.ok(liveCourseAccount);
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,@PathVariable Long limit){
        Page<LiveCourse> pageParam = new Page<>(page, limit);
        IPage<LiveCourse> pageModel = liveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo){
            liveCourseService.saveLive(liveCourseFormVo);
            return Result.ok(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        liveCourseService.removeLive(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        LiveCourse liveCourse = liveCourseService.getById(id);
        return Result.ok(liveCourse);
    }

    @ApiOperation(value = "获取")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id){
        LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveCourseFormVo(id);
        return Result.ok(liveCourseFormVo);
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody LiveCourseFormVo liveCourseFormVo){
        liveCourseService.updateLiveById(liveCourseFormVo);
        return Result.ok(null);
    }

}

