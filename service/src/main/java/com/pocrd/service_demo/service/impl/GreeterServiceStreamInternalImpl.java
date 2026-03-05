package com.pocrd.service_demo.service.impl;

import com.pocrd.service_demo.api.GreeterServiceStreamInternal;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * GreeterService Stream Internal Implementation
 * 
 * 此实现类暴露流式接口到 Triple 协议（端口 50051）
 * 供内部服务间流式通信使用
 */
@DubboService(version = "1.0.0", group = "internal", registry = "nacos-internal", protocol = "dubbo")
public class GreeterServiceStreamInternalImpl implements GreeterServiceStreamInternal {
    
    @Override
    public StreamObserver<String> greetInteractive(StreamObserver<String> responseObserver) {
        return new StreamObserver<String>() {
            @Override
            public void onNext(String name) {
                String response = String.format("[Internal Interactive] Greeting for: %s, received at: %s, from: %s", 
                        name, System.currentTimeMillis(), RpcContext.getContext().getLocalAddressString());
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error in interactive greeting: " + throwable.getMessage());
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
