package com.pocrd.service_demo.api;

import org.apache.dubbo.common.stream.StreamObserver;

/**
 * Greeter service stream interface - Internal Streaming Only
 * 
 * 此接口定义流式方法，仅在内网供其他 Dubbo 服务调用
 * 不会通过 Higress 网关暴露给公网
 * 使用 Triple 协议进行流式通信
 */
public interface GreeterServiceStreamInternal {
    
    /**
     * 双向流式交互问候 - 内部服务间实时通信
     * @param observer 流式响应观察者
     * @return 流式请求观察者
     */
    StreamObserver<String> greetInteractive(StreamObserver<String> observer);
}
