package com.pocrd.service_demo.service.impl;

import com.pocrd.service_demo.api.GreeterServiceHttpExport;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.TimeUnit;

/**
 * GreeterService HTTP Export Implementation
 * 
 * 此实现类提供通过 Higress 网关暴露给公网的 HTTP 接口
 * 使用 Dubbo Triple 协议（兼容 gRPC/HTTP2）
 */
@DubboService(version = "1.0.0", group = "default")
public class GreeterServiceHttpExportImpl implements GreeterServiceHttpExport {

    @Override
    public String greet(String name) {
        // Get current RPC context information
        String remoteAddress = RpcContext.getContext().getRemoteAddressString();
        String localAddress = RpcContext.getContext().getLocalAddressString();
        
        // Return greeting message with additional context info
        return String.format("Hello %s, from %s (to %s)", 
                name, remoteAddress, localAddress);
    }

    @Override
    public String greet2(String name1, String name2) {
        // Get current RPC context information
        String remoteAddress = RpcContext.getContext().getRemoteAddressString();
        String localAddress = RpcContext.getContext().getLocalAddressString();
        
        // Return greeting message for two names
        return String.format("Hello %s and %s, from %s (to %s)", 
                name1, name2, remoteAddress, localAddress);
    }

    @Override
    public void greetStream(String name, StreamObserver<String> observer) {
        try {
            // Send greetings multiple times with delay
            for (int i = 1; i <= 5; i++) {
                String greeting = String.format("Hello %s! This is greeting #%d from %s", 
                        name, i, RpcContext.getContext().getLocalAddressString());
                observer.onNext(greeting);
                
                // Simulate some processing time
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            observer.onCompleted();
        } catch (Exception e) {
            observer.onError(e);
        }
    }

}
