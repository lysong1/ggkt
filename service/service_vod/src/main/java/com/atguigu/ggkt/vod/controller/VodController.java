package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuyusong
 * @create 2022-09-13 15:09
 */
@Api(tags = "腾讯云点播")
@RestController
@RequestMapping("/admin/vod")
//@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    @ApiOperation("上传视频接口")
    @PostMapping("upload")
    public Result upload(){
       String fileId =  vodService.updateVideo();
        return Result.ok(fileId);
    }


    @DeleteMapping("remove/{fileId}")
    public Result removeVideo(@PathVariable String fileId){
        vodService.removeVideo(fileId);
        return Result.ok(null);
    }

}
