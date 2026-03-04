package com.pocrd.service_demo.client;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import com.pocrd.service_demo.api.GreeterServiceHttpExport;
import com.pocrd.service_demo.api.GreeterServiceInternal;

/**
 * 测试配置管理器
 * 
 * 支持两种测试模式：
 * 1. HTTP 模式 - 通过 Higress 网关调用
 * 2. Dubbo RPC 模式 - 直接通过 Dubbo 原生协议调用
 */
public class TestConfigManager {
    
    private static final String TEST_MODE = System.getProperty("test.mode", "http");
    private static final String HIGRESS_URL = System.getProperty("higress.url", "http://localhost:80");
    private static final String DUBBO_URL = System.getProperty("dubbo.url", "tri://localhost:50051");
    private static final String NACOS_ADDRESS = System.getProperty("nacos.address", "nacos://localhost:8848");
    
    private GreeterServiceHttpExport greeterService;
    private GreeterServiceInternal greeterServiceInternal;
    private boolean initialized = false;
    
    /**
     * 初始化测试客户端
     */
    public void init() {
        if (initialized) {
            return;
        }
        
        System.out.println("===========================================");
        System.out.println("初始化测试客户端");
        System.out.println("测试模式：" + TEST_MODE);
        System.out.println("===========================================");
        
        if ("http".equalsIgnoreCase(TEST_MODE)) {
            initHttpClient();
        } else if ("dubbo".equalsIgnoreCase(TEST_MODE)) {
            initDubboClient();
        } else {
            throw new IllegalArgumentException("不支持的测试模式：" + TEST_MODE);
        }
        
        initialized = true;
    }
    
    /**
     * 初始化 HTTP 客户端（用于通过 Higress 网关调用）
     */
    private void initHttpClient() {
        System.out.println("使用 HTTP 模式通过 Higress 网关调用服务");
        System.out.println("Higress 网关地址：" + HIGRESS_URL);
        System.out.println("注意：HTTP 模式下需要使用 HttpClientUtils 进行调用");
    }
    
    /**
     * 初始化 Dubbo RPC 客户端
     * 直连模式：跳过注册中心，直接通过 DUBBO_URL 连接服务
     */
    private void initDubboClient() {
        System.out.println("使用 Dubbo RPC 直连模式");
        System.out.println("Dubbo 服务地址：" + DUBBO_URL);
        
        // 配置 Dubbo Application
        ApplicationConfig applicationConfig = new ApplicationConfig("client-test-consumer");
        applicationConfig.setQosEnable(false);
        
        // 使用直连注册中心（N/A，跳过服务发现）
        RegistryConfig registryConfig = new RegistryConfig("N/A");
        
        // 配置 HttpExport 服务 Reference（直连）
        ReferenceConfig<GreeterServiceHttpExport> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(GreeterServiceHttpExport.class);
        referenceConfig.setUrl(DUBBO_URL);
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGroup("default");
        
        // 配置 Internal 服务 Reference（直连）
        ReferenceConfig<GreeterServiceInternal> internalReferenceConfig = new ReferenceConfig<>();
        internalReferenceConfig.setInterface(GreeterServiceInternal.class);
        internalReferenceConfig.setUrl(DUBBO_URL);
        internalReferenceConfig.setVersion("1.0.0");
        internalReferenceConfig.setGroup("internal");
        
        // 启动 Dubbo
        DubboBootstrap.getInstance()
                .application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .reference(internalReferenceConfig)
                .start();
        
        // 获取服务代理
        this.greeterService = referenceConfig.get();
        this.greeterServiceInternal = internalReferenceConfig.get();
        
        System.out.println("Dubbo 客户端初始化完成");
    }
    
    /**
     * 获取 GreeterService 实例
     * 
     * @return GreeterService 实例（仅在 Dubbo RPC 模式下可用）
     */
    public GreeterServiceHttpExport getGreeterService() {
        if (!initialized) {
            init();
        }
        
        if ("dubbo".equalsIgnoreCase(TEST_MODE)) {
            return greeterService;
        } else {
            throw new IllegalStateException("HTTP 模式下无法直接获取 GreeterService，请使用 HttpClientUtils");
        }
    }
    
    /**
     * 获取 GreeterServiceInternal 实例
     * 
     * @return GreeterServiceInternal 实例（仅在 Dubbo RPC 模式下可用）
     */
    public GreeterServiceInternal getGreeterServiceInternal() {
        if (!initialized) {
            init();
        }
        
        if ("dubbo".equalsIgnoreCase(TEST_MODE)) {
            return greeterServiceInternal;
        } else {
            throw new IllegalStateException("HTTP 模式下无法直接获取 GreeterServiceInternal，请使用 Dubbo RPC 模式");
        }
    }
    
    /**
     * 判断当前是否为 HTTP 模式
     */
    public boolean isHttpMode() {
        return "http".equalsIgnoreCase(TEST_MODE);
    }
    
    /**
     * 判断当前是否为 Dubbo RPC 模式
     */
    public boolean isDubboMode() {
        return "dubbo".equalsIgnoreCase(TEST_MODE);
    }
    
    /**
     * 获取 Higress 网关地址
     */
    public String getHigressUrl() {
        return HIGRESS_URL;
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
