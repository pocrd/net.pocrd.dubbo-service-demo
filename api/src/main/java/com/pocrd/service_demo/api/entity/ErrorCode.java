package com.pocrd.service_demo.api.entity;

import com.pocrd.api_publish_service.sdk.entity.AbstractReturnCode;

public class ErrorCode extends AbstractReturnCode {
    public static final ErrorCode UNKNOWN_ERROR = new ErrorCode("Unknown Error", 10000);

    public ErrorCode(String message, int code) {
        super(message, code);
    }
}
