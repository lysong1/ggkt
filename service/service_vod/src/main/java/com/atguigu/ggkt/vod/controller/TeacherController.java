package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-30
 */
//@CrossOrigin
@Api(tags = "讲师管理接口")
@RestController
@RequestMapping(value="/admin/vod/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;




    @ApiOperation("查询所有讲师")
    @GetMapping("findAll")
    public Result  findAll(){
        List<Teacher> list = teacherService.list();
        return  Result.ok(list).message("查询数据成功");
    }

    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public Result removeById(@ApiParam(name = "id",value = "ID", required = true) @PathVariable("id") String id){

        boolean isSuccess = teacherService.removeById(id);
        if (isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    @ApiOperation(value = "获取分页列表")
    @PostMapping("findQueryPage/{current}/{limit}")
    public Result index(
           @PathVariable("current") Long current,
           @PathVariable("limit") Long limit,
           @RequestBody(required = false) TeacherQueryVo teacherQueryVo
    ){
        Page<Teacher> pageParam= new Page<>(current, limit);
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            teacherQueryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)){
            teacherQueryWrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(joinDateBegin)){
            teacherQueryWrapper.ge("join_date", joinDateBegin);
        }
        if (!StringUtils.isEmpty(joinDateEnd)){
            teacherQueryWrapper.le("join_date",joinDateEnd);
        }
        Page<Teacher> teacherPage = teacherService.page(pageParam, teacherQueryWrapper);
        return Result.ok(teacherPage);
    }


    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Teacher teacher){
        teacherService.save(teacher);
        return Result.ok(null);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id") Long id){
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);
    }

    //远程调用
    @ApiOperation(value = "获取")
    @GetMapping("inner/get/{id}")
    public Teacher getTeacherInfo(@PathVariable("id") Long id){
        Teacher teacher = teacherService.getById(id);
        return teacher;
    }

    @ApiOperation("修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Teacher teacher){
        teacherService.updateById(teacher);
        return Result.ok(null);
    }

    @ApiOperation("根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        teacherService.removeByIds(ids);
        return Result.ok(null);
    }

}

