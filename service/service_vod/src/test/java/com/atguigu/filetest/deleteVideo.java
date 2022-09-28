package com.atguigu.filetest;

import com.atguigu.ggkt.vod.utils.ConstantPropertiesUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosRequest;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosResponse;

/**
 * @author liuyusong
 * @create 2022-09-14 21:41
 */
public class deleteVideo {
    public static void main(String[] args) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,
        Credential cred = new Credential("AKIDqWyDF3e1A6nHWXhkeKmW3LODoUw99wVp", "93jBONbeS2NVPo0yjFY4RcbP1OWRVO4W");
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        VodClient client = new VodClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeMediaInfosRequest req = new DescribeMediaInfosRequest();
        String[] fileIds1 = {"387702305980850212"};
        req.setFileIds(fileIds1);

        // 返回的resp是一个DescribeMediaInfosResponse的实例，与请求对象对应
        DescribeMediaInfosResponse resp = client.DescribeMediaInfos(req);
        // 输出json格式的字符串回包
        System.out.println(DescribeMediaInfosResponse.toJsonString(resp));
    }
}
