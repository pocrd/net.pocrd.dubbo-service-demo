package com.pocrd.service_demo.client;

import com.pocrd.service_demo.api.GreeterServiceHttpExport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GreeterService 集成测试
 * 
 * 同时测试两种调用方式：
 * 1. HTTP 模式 - 通过 Higress 网关调用（Triple 协议，端口 50051）
 * 2. Dubbo RPC 模式 - 直接通过 Dubbo 协议调用（端口 50052，Fury 序列化）
 */
@DisplayName("GreeterService 集成测试")
public class GreeterServiceTest {
    
    private static HttpClientUtils httpClient;
    private static TestConfigManager configManager;
    private static GreeterServiceHttpExport dubboService;
    
    @BeforeAll
    static void setUp() {
        // 初始化 HTTP 客户端（连接 Higress 网关，端口 80）
        httpClient = new HttpClientUtils("http://localhost:80");
        
        // 初始化 Dubbo 客户端（通过 TestConfigManager）
        configManager = new TestConfigManager();
        configManager.init();
        dubboService = configManager.getGreeterService();
    }
    
    @AfterAll
    static void tearDown() {
        if (configManager != null) {
            configManager.destroy();
        }
    }
    
    /**
     * HTTP 模式测试 - 通过 Higress 网关调用（Triple 协议，端口 50051）
     */
    @Test
    @DisplayName("HTTP 模式 - Greet 测试")
    void testGreetHttpMode() throws Exception {
        String response = httpClient.greet("World");
        
        assertNotNull(response);
        System.out.println("HTTP 模式响应：" + response);
        assertTrue(response.contains("World"), "响应应该包含 'World'");
        System.out.println("✓ HTTP 模式测试通过");
    }
    
    /**
     * Dubbo RPC 模式测试 - 直接调用 Dubbo 服务（端口 50052，Fury 序列化）
     */
    @Test
    @DisplayName("Dubbo RPC 模式 - Greet 测试")
    void testGreetDubboMode() {
        String response = dubboService.greet("Dubbo RPC World");
        
        assertNotNull(response);
        System.out.println("Dubbo RPC 模式响应：" + response);
        assertTrue(response.contains("Dubbo RPC World"));
        System.out.println("✓ Dubbo RPC 模式测试通过");
    }
}
