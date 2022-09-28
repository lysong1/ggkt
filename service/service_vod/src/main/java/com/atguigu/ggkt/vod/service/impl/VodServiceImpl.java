package com.atguigu.ggkt.vod.service.impl;



import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vod.service.VideoService;
import com.atguigu.ggkt.vod.service.VodService;
import com.atguigu.ggkt.vod.utils.ConstantPropertiesUtil;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosRequest;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyusong
 * @create 2022-09-13 15:10
 */

@Service
public class VodServiceImpl implements VodService {

    @Autowired
    private VideoService videoService;

    @Value("${tencent.video.appid}")
    private String appId;

    @Override
    public String updateVideo() {

        VodUploadClient client = new VodUploadClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        VodUploadRequest request = new VodUploadRequest();
        request.setMediaFilePath("F:\\test.mp4");
        request.setProcedure("LongVideoPreset");
        try {
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            String fileId = response.getFileId();
            return fileId;
        } catch (Exception e) {
            // 业务方进行异常处理
            throw new GgktException(20001, "上传视频失败");
        }
    }

    @Override
    public void removeVideo(String fileId) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,
            Credential cred = new Credential(ConstantPropertiesUtil.ACCESS_KEY_ID
                    , ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            String fileIds1 = fileId;
            req.setFileId(fileIds1);

            // 返回的resp是一个DescribeMediaInfosResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);
            // 输出json格式的字符串回包
            System.out.println(DescribeMediaInfosResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            throw new GgktException(20001, "删除视频失败");
        }
    }

    @Override
    public Map<String, Object> getPlayAuth(Long courseId, Long videoId) {
        Video video = videoService.getById(videoId);
        if (video == null){
            throw new GgktException(20001, "小节信息不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("videoSourceId",video.getVideoSourceId());
        map.put("appId",appId);
        return map;
    }
}
