package com.atguigu.ggkt.order.service;


import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-16
 */
public interface OrderInfoService extends IService<OrderInfo> {


    Map<String, Object> findPageOrderInfo(Page<OrderInfo> orderInfoPage, OrderInfoQueryVo orderInfoQueryVo);

    Long submintOrser(OrderFormVo orderFormVo);
}
