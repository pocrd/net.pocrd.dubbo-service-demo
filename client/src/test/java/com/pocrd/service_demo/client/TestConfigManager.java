package com.pocrd.service_demo.client;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import com.pocrd.service_demo.api.GreeterServiceHttpExport;
import com.pocrd.service_demo.api.GreeterServiceInternal;
import com.pocrd.service_demo.api.GreeterServiceStreamInternal;

/**
 * Dubbo RPC 客户端配置管理器
 * 
 * 用于测试类中初始化 Dubbo 客户端，直连服务进行 RPC 调用
 */
public class TestConfigManager {
    
    private String getDubboUrl() {
        return System.getProperty("dubbo.url", "dubbo://localhost:50052?serialization=fury");
    }
    
    private GreeterServiceHttpExport greeterService;
    private GreeterServiceInternal greeterServiceInternal;
    private GreeterServiceStreamInternal greeterServiceStreamInternal;
    private boolean initialized = false;
    
    /**
     * 初始化 Dubbo RPC 客户端
     */
    public void init() {
        if (initialized) {
            return;
        }
        
        System.out.println("===========================================");
        System.out.println("初始化 Dubbo RPC 客户端");
        System.out.println("Dubbo 服务地址：" + getDubboUrl());
        System.out.println("===========================================");
        
        initDubboClient();
        
        initialized = true;
    }
    
    /**
     * 初始化 Dubbo RPC 客户端
     * 直连模式：跳过注册中心，直接通过 DUBBO_URL 连接服务
     */
    private void initDubboClient() {
        String dubboUrl = getDubboUrl();
        System.out.println("使用 Dubbo RPC 直连模式");
        System.out.println("Dubbo 服务地址：" + dubboUrl);
        
        // 配置 Dubbo Application
        ApplicationConfig applicationConfig = new ApplicationConfig("client-test-consumer");
        applicationConfig.setQosEnable(false);
        
        // 使用直连注册中心（N/A，跳过服务发现）
        RegistryConfig registryConfig = new RegistryConfig("N/A");
        
        // 配置 HttpExport 服务 Reference（直连）
        ReferenceConfig<GreeterServiceHttpExport> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(GreeterServiceHttpExport.class);
        referenceConfig.setUrl(dubboUrl);
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGroup("default");

        
        // 配置 Internal 服务 Reference（直连）
        ReferenceConfig<GreeterServiceInternal> internalReferenceConfig = new ReferenceConfig<>();
        internalReferenceConfig.setInterface(GreeterServiceInternal.class);
        internalReferenceConfig.setUrl(dubboUrl);
        internalReferenceConfig.setVersion("1.0.0");
        internalReferenceConfig.setGroup("internal");
        
        // 启动 Dubbo
        DubboBootstrap bootstrap = DubboBootstrap.getInstance()
                .application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .reference(internalReferenceConfig);
        
        // 如果 URL 是 Triple 协议，也配置 Stream 服务
        if (dubboUrl.startsWith("tri://")) {
            ReferenceConfig<GreeterServiceStreamInternal> streamReferenceConfig = new ReferenceConfig<>();
            streamReferenceConfig.setInterface(GreeterServiceStreamInternal.class);
            streamReferenceConfig.setUrl(dubboUrl);
            streamReferenceConfig.setVersion("1.0.0");
            streamReferenceConfig.setGroup("internal");
            bootstrap.reference(streamReferenceConfig);
            bootstrap.start();
            this.greeterServiceStreamInternal = streamReferenceConfig.get();
        } else {
            bootstrap.start();
        }
        
        // 获取服务代理
        this.greeterService = referenceConfig.get();
        this.greeterServiceInternal = internalReferenceConfig.get();
        
        System.out.println("Dubbo 客户端初始化完成");
    }
    
    /**
     * 获取 GreeterService 实例
     * 
     * @return GreeterService 实例
     */
    public GreeterServiceHttpExport getGreeterService() {
        if (!initialized) {
            init();
        }
        return greeterService;
    }
    
    /**
     * 获取 GreeterServiceInternal 实例
     * 
     * @return GreeterServiceInternal 实例
     */
    public GreeterServiceInternal getGreeterServiceInternal() {
        if (!initialized) {
            init();
        }
        return greeterServiceInternal;
    }
    
    /**
     * 获取 GreeterServiceStreamInternal 实例
     * 
     * @return GreeterServiceStreamInternal 实例
     */
    public GreeterServiceStreamInternal getGreeterServiceStreamInternal() {
        if (!initialized) {
            init();
        }
        return greeterServiceStreamInternal;
    }
    
    /**
     * 关闭资源
     */
    public void destroy() {
        if (greeterService != null) {
            try {
                DubboBootstrap.getInstance().stop();
            } catch (Exception e) {
                System.err.println("关闭 Dubbo 时出错：" + e.getMessage());
            }
        }
    }
}
