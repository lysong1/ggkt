package com.atguigu.ggkt.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuyusong
 * @create 2022-09-08 19:22
 */
public interface FileService {

   public String upload(MultipartFile file);
}
