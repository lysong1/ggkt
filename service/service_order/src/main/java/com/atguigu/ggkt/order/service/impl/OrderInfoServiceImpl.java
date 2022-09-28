package com.atguigu.ggkt.order.service.impl;


import com.atguigu.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.client.activity.CouponInfoFeignClient;
import com.atguigu.ggkt.client.course.CourseFeignClient;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.order.OrderDetail;
import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.order.mapper.OrderInfoMapper;
import com.atguigu.ggkt.order.service.OrderDetailService;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.utils.AuthContextHolder;
import com.atguigu.ggkt.utils.OrderNoUtils;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-16
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private CouponInfoFeignClient couponInfoFeignClient;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;


    @Override
    public Map<String, Object> findPageOrderInfo(Page<OrderInfo> orderInfoPage, OrderInfoQueryVo orderInfoQueryVo) {
        Long userId = orderInfoQueryVo.getUserId();
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();
        String phone = orderInfoQueryVo.getPhone();
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        if(!StringUtils.isEmpty(orderStatus)) {
            queryWrapper.eq("order_status",orderStatus);
        }
        if(!StringUtils.isEmpty(userId)) {
            queryWrapper.eq("user_id",userId);
        }
        if(!StringUtils.isEmpty(outTradeNo)) {
            queryWrapper.eq("out_trade_no",outTradeNo);
        }
        if(!StringUtils.isEmpty(phone)) {
            queryWrapper.eq("phone",phone);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            queryWrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            queryWrapper.le("create_time",createTimeEnd);
        }

        Page<OrderInfo> pages = baseMapper.selectPage(orderInfoPage, queryWrapper);
        long totalCount = pages.getTotal();
        long pageCount = pages.getPages();
        List<OrderInfo> records = pages.getRecords();

        records.stream().forEach(item ->{
            this.getOrderDetail(item);
        });

        Map<String, Object> map = new HashMap<>();
        map.put("total",totalCount);
        map.put("pageCount",pageCount);
        map.put("records",records);

        return map;
    }

    @Override
    public Long submintOrser(OrderFormVo orderFormVo) {
        //1 获取生成订单条件值
        Long courseId = orderFormVo.getCourseId();
        Long couponId = orderFormVo.getCouponId();
        Long userId = AuthContextHolder.getUserId();

        //2 判断当前用户是否已经生成订单
        //课程ID
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getUserId,userId);
        wrapper.eq(OrderDetail::getCourseId,courseId);
        OrderDetail orderDetailExists = orderDetailService.getOne(wrapper);
        if (orderDetailExists != null){
            return orderDetailExists.getId();//订单已存在，直接返回订单id
        }

        //3 根据课程ID查询课程信息
        Course course = courseFeignClient.getById(courseId);
        if (course == null){
            throw new GgktException(20001, "课程不存在");
        }
        //4 根据用户ID查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if (userInfo == null){
            throw new GgktException(20001,"用户不存在");
        }
        //5 根据优惠券ID查询优惠券信息
        BigDecimal couponReduce = new BigDecimal(0);
        if (couponId != null){
            CouponInfo couponInfo = couponInfoFeignClient.getById(couponId);
            couponReduce = couponInfo.getAmount();
        }

        //6 封装订单生成需要数据到对象，完成添加订单
        //6.1 封装数据到OrderInfo对象里面，添加订单基本信息表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());
        orderInfo.setTradeBody(course.getTitle());
        orderInfo.setOrderStatus("0");
        this.save(orderInfo);

        //6.1 封装数据到OrderDetail对象里面，添加订单详情信息表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(new BigDecimal(0));
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));

        orderDetailService.save(orderDetail);

        //7 对更新优惠券状态
        if(null != orderFormVo.getCouponUseId()) {
            couponInfoFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(), orderInfo.getId());
        }
        //8 返回订单ID
        return orderInfo.getId();
    }

    private OrderInfo getOrderDetail(OrderInfo orderInfo) {
        Long userId = orderInfo.getUserId();
        OrderDetail orderDetail = orderDetailService.getById(userId);
        if (orderDetail != null) {
            String courseName = orderDetail.getCourseName();
            orderInfo.getParam().put("courseName",courseName);
        }
        return orderInfo;
    }
}
