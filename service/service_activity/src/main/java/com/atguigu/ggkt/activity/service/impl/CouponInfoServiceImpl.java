package com.atguigu.ggkt.activity.service.impl;
import com.atguigu.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.activity.mapper.CouponInfoMapper;
import com.atguigu.ggkt.activity.service.CouponInfoService;
import com.atguigu.ggkt.activity.service.CouponUseService;
import com.atguigu.ggkt.model.activity.CouponUse;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.vo.activity.CouponUseQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.atguigu.ggkt.model.activity.CouponInfo;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-09-16
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponUseService couponUseService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    @Override
    public IPage<CouponUse> selectPage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo) {

        Long couponId = couponUseQueryVo.getCouponId();
        String couponStatus = couponUseQueryVo.getCouponStatus();
        String getTimeBegin = couponUseQueryVo.getGetTimeBegin();
        String getTimeEnd = couponUseQueryVo.getGetTimeEnd();

        QueryWrapper<CouponUse> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(couponId)){
            queryWrapper.eq("coupon_id", couponId);
        }
        if (!StringUtils.isEmpty(couponStatus)){
            queryWrapper.eq("coupon_status", couponStatus);
        }
        if (!StringUtils.isEmpty(getTimeBegin)){
            queryWrapper.ge("get_time", getTimeBegin);
        }
        if (!StringUtils.isEmpty(getTimeEnd)){
            queryWrapper.le("get_time", getTimeEnd);
        }

        IPage<CouponUse> page = couponUseService.page(pageParam, queryWrapper);

        List<CouponUse> couponUseList = page.getRecords();
        couponUseList.stream().forEach(item ->{
            this.getgetUserInfoBycouponUse(item);
        });

        return page;
    }

    @Override
    public void updateCouponInfoUseStatus(Long couponUseId, Long orderId) {
        CouponUse couponUse = new CouponUse();
        couponUse.setId(couponUseId);
        couponUse.setOrderId(orderId);
        couponUse.setCouponStatus("1");
        couponUse.setUsingTime(new Date());
        couponUseService.updateById(couponUse);
    }

    private CouponUse getgetUserInfoBycouponUse(CouponUse couponUse) {
        Long userId = couponUse.getUserId();
        if (!StringUtils.isEmpty(userId)){
            UserInfo userInfo = userInfoFeignClient.getById(userId);
            if (userInfo != null){
                couponUse.getParam().put("nickName", userInfo.getNickName());
                couponUse.getParam().put("phone", userInfo.getPhone());
            }
        }

        return couponUse;
    }
}
