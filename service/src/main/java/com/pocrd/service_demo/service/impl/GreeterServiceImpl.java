package com.pocrd.service_demo.service.impl;

import com.pocrd.service_demo.api.GreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

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
}
