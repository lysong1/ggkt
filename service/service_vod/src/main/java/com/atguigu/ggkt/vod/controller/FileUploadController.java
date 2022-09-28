package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuyusong
 * @create 2022-09-08 19:22
 */
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/admin/vod/file")
//@CrossOrigin
public class FileUploadController {

    @Autowired
    private FileService fileService;


    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public Result upload(@RequestParam("file") MultipartFile file){

       String url = fileService.upload(file);

        return Result.ok(url).message("文件上传成功");
    }

}
