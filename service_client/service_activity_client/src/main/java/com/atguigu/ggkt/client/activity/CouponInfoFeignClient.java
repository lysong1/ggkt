package com.atguigu.ggkt.client.activity;

import com.atguigu.ggkt.model.activity.CouponInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liuyusong
 * @create 2022-09-23 19:51
 */
@FeignClient("service-activity")
public interface CouponInfoFeignClient {

    @GetMapping(value = "/api/activity/couponInfo/inner/getById/{couponId}")
    CouponInfo getById(@PathVariable("couponId") Long couponId);

    @GetMapping(value = "/api/activity/couponInfo/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId,
                                      @PathVariable("orderId") Long orderId);

}
