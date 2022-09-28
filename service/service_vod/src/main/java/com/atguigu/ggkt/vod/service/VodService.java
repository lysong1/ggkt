package com.atguigu.ggkt.vod.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liuyusong
 * @create 2022-09-13 15:10
 */
@Service
public interface VodService {

    String updateVideo();

    void removeVideo(String fileId);

    Map<String, Object> getPlayAuth(Long courseId, Long videoId);

}
