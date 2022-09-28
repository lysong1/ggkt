package com.atguigu.ggkt.wechat.service;

import java.util.Map;

/**
 * @author liuyusong
 * @create 2022-09-19 22:44
 */
public interface MessageService {
    //接收消息
    String receiveMessage(Map<String, String> param);

    void pushPayMessage(Long orderId);
}
