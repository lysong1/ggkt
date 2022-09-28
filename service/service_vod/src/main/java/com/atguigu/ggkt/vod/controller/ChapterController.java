package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vod.service.ChapterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-09-11
 */
//@CrossOrigin
@RestController
@RequestMapping(value="/admin/vod/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    //1 课程大纲列表（章节和小节列表）
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getTreeList(@PathVariable Long courseId){
       List<ChapterVo> chapterVoList = chapterService.getTreeList(courseId);
       return Result.ok(chapterVoList);
    }

    //2 添加章节
    @ApiOperation("添加")
    @PostMapping("save")
    public Result save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return Result.ok(null);
    }

    //3 修改-根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Chapter chapter = chapterService.getById(id);
        return Result.ok(chapter);
    }

    //4修改-最终实现
    @ApiOperation("修改")
    @PostMapping("update")
    public Result update(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return Result.ok(null);
    }

    //5 删除
    @ApiOperation("删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        chapterService.removeById(id);
        return Result.ok(null);
    }
}

