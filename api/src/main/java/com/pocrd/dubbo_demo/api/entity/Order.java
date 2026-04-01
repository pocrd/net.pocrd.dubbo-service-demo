package com.pocrd.dubbo_demo.api.entity;

import java.io.Serializable;

import com.pocrd.api_publish_service.sdk.annotation.Description;

/**
 * Order entity for API layer
 */
@Description("订单信息")
public record Order(
    @Description("订单ID") Long id,
    @Description("订单编号") String orderNo,
    @Description("用户ID") Long userId,
    @Description("订单金额") Integer amount,
    @Description("订单状态") Integer status,
    @Description("备注") String remark
) implements Serializable {
    private static final long serialVersionUID = 1L;
}