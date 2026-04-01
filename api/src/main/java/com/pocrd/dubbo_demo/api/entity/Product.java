package com.pocrd.dubbo_demo.api.entity;

import com.pocrd.api_publish_service.sdk.annotation.Description;

import java.io.Serializable;

/**
 * Product entity for API layer
 */
@Description("产品信息")
public record Product(
    @Description("产品ID") Long id,
    @Description("产品编码") String productCode,
    @Description("产品名称") String productName,
    @Description("价格") Integer price,
    @Description("库存") Integer stock,
    @Description("分类") String category
) implements Serializable {
    private static final long serialVersionUID = 1L;
}