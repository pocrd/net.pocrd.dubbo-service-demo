package com.example.dubboservice.api;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

/**
 * Example service interface definition
 * Using Dubbo Triple protocol
 */
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
