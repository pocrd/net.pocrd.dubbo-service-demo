package com.pocrd.dubbo_demo.api.entity;

import com.pocrd.api_publish_service.sdk.annotation.Description;

import java.io.Serializable;

/**
 * User entity for API layer
 */
@Description("用户信息")
public record User(
    @Description("用户ID") Long id,
    @Description("用户名") String username, 
    @Description("邮箱") String email,
    @Description("电话") String phone
) implements Serializable {
    private static final long serialVersionUID = 1L;
}