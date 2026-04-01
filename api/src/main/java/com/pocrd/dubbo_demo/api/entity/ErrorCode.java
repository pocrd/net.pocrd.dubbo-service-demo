package com.pocrd.dubbo_demo.api.entity;

import com.pocrd.api_publish_service.sdk.entity.AbstractReturnCode;

/**
 * 错误码定义
 * 
 * 错误码范围: [200000, 209999] - 用于 CRUDService
 */
public class ErrorCode extends AbstractReturnCode {
    
    // CRUDService 错误码范围 [200000, 209999]
    public static final ErrorCode UNKNOWN_ERROR = new ErrorCode("未知错误", 200000);
    public static final ErrorCode PARAM_ERROR = new ErrorCode("参数错误", 200001);
    public static final ErrorCode NOT_FOUND = new ErrorCode("记录不存在", 200002);

    public ErrorCode(String message, int code) {
        super(message, code);
    }
}
