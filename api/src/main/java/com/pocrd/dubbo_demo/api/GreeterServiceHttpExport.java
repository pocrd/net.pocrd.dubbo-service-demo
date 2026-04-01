package com.pocrd.dubbo_demo.api;

import com.pocrd.dubbo_demo.api.entity.GreeterErrorCode;

import com.pocrd.api_publish_service.sdk.annotation.ApiGroup;
import com.pocrd.api_publish_service.sdk.annotation.Description;
import com.pocrd.api_publish_service.sdk.annotation.ApiParameter;

import org.apache.dubbo.common.stream.StreamObserver;

/**
 * Greeter service interface definition - HTTP Export via Higress Gateway
 *
 * 此接口定义的方法将通过 Higress 网关暴露给公网 HTTP 访问
 * 使用 Dubbo Triple 协议（兼容 gRPC/HTTP2）
 */
@ApiGroup(name="GreeterService", minCode=100000, maxCode=109999, codeDefine=GreeterErrorCode.class)
@Description("Greeter服务，提供问候功能")
public interface GreeterServiceHttpExport {
    /**
     * Unary call - simple greeting
     * @param name the person's name
     * @return greeting message
     */
    @Description("简单问候")
    String greet(@ApiParameter(desc = "姓名", required = true) String name);

    @Description("双重问候")
    String greet2(@ApiParameter(desc = "姓名1", required = true) String name1, 
                  @ApiParameter(desc = "姓名2", required = true) String name2);
    
    /**
     * Server streaming - greet multiple times
     * @param name the person's name
     * @param observer the stream observer to send multiple greetings
     */
    @Description("流式问候")
    void greetStream(@ApiParameter(desc = "姓名", required = true) String name, 
                     @ApiParameter(desc = "流观察者", required = true) StreamObserver<String> observer);
    
}
