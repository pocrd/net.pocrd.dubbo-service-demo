package com.pocrd.service_demo.service.impl;

import com.pocrd.service_demo.api.GreeterServiceHttpExport;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.TimeUnit;

/**
 * GreeterService HTTP Export Implementation
 * 
 * 此实现类暴露到 Triple 协议（端口 50051）：供 Higress 网关 HTTP 调用
 */
@DubboService(version = "1.0.0", group = "public", registry = "nacos-public", protocol = "tri", path = "api")
public class GreeterServiceHttpExportImpl implements GreeterServiceHttpExport {

    @Override
    public String greet(String name) {
        // Get current RPC context information
        String remoteAddress = RpcContext.getServiceContext().getRemoteAddressString();
        String localAddress = RpcContext.getServiceContext().getLocalAddressString();

        // Get device ID from HTTP headers
        String deviceId = RpcContext.getServiceContext().getAttachment("x-dubbo-device-id");
        if (deviceId == null) {
            deviceId = "unknown";
        }

        // Return greeting message with additional context info
        return String.format("Hello %s, from %s (to %s), device: %s",
                name, remoteAddress, localAddress, deviceId);
    }

    @Override
    public String greet2(String name1, String name2) {
        // Get current RPC context information
        String remoteAddress = RpcContext.getServiceContext().getRemoteAddressString();
        String localAddress = RpcContext.getServiceContext().getLocalAddressString();

        // Get device ID from HTTP headers
        String deviceId = RpcContext.getServiceContext().getAttachment("x-dubbo-device-id");
        if (deviceId == null) {
            deviceId = "unknown";
        }
        
        // Return greeting message for two names with device ID
        return String.format("Hello %s and %s, from %s (to %s), device: %s", 
                name1, name2, remoteAddress, localAddress, deviceId);
    }

    @Override
    public void greetStream(String name, StreamObserver<String> observer) {
        try {
            // Get device ID from HTTP headers
            String deviceId = RpcContext.getServiceContext().getAttachment("x-dubbo-device-id");
            if (deviceId == null) {
                deviceId = "unknown";
            }

            // Send greetings multiple times with delay
            for (int i = 1; i <= 5; i++) {
                String greeting = String.format("Hello %s! This is greeting #%d from %s, device: %s",
                        name, i, RpcContext.getServiceContext().getLocalAddressString(), deviceId);
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
