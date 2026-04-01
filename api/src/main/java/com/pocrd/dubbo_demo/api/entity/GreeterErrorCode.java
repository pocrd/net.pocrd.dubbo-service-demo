package com.pocrd.dubbo_demo.api.entity;

import com.pocrd.api_publish_service.sdk.entity.AbstractReturnCode;

/**
 * GreeterService 错误码定义
 * 
 * 错误码范围: [100000, 109999]
 */
public class GreeterErrorCode extends AbstractReturnCode {
    
    // GreeterService 错误码范围 [100000, 109999]
    public static final GreeterErrorCode UNKNOWN_ERROR = new GreeterErrorCode("未知错误", 100000);
    public static final GreeterErrorCode PARAM_ERROR = new GreeterErrorCode("参数错误", 100001);

    public GreeterErrorCode(String message, int code) {
        super(message, code);
    }
}
