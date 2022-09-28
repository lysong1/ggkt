package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liuyusong
 * @create 2022-09-09 9:28
 */
@Api("课程分类管理")
@RestController
//@CrossOrigin
@RequestMapping(value = "/admin/vod/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("getChildSubject/{id}")
    public Result getChildSubject(@PathVariable("id") Long id){
       List<Subject> list  = subjectService.selectList(id);
       return Result.ok(list);
    }


    @ApiOperation(value="导出")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response){

        subjectService.exportData(response);
    }


    @PostMapping("importData")
    public Result importData(MultipartFile file){
        subjectService.importData(file);
        return Result.ok(null);
    }

}
