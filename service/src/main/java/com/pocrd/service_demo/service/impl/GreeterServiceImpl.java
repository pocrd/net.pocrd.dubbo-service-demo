package com.pocrd.service_demo.service.impl;

import com.pocrd.service_demo.api.GreeterService;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@DubboService(version = "1.0.0", group = "default")
public class GreeterServiceImpl implements GreeterService {

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

    @Override
    public StreamObserver<String> greetInteractive(StreamObserver<String> responseObserver) {
        return new StreamObserver<String>() {
            @Override
            public void onNext(String name) {
                String response = String.format("Interactive greeting for: %s, received at: %s, from: %s", 
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
