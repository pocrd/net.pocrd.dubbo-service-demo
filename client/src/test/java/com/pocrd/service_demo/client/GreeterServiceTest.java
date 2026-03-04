package com.pocrd.service_demo.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GreeterService 集成测试
 * 
 * 支持两种测试模式：
 * 1. HTTP 模式 - 通过 Higress 网关调用 GreeterServiceHttpExport
 * 2. Dubbo RPC 模式 - 直接通过 Dubbo 原生协议调用 GreeterServiceHttpExport
 */
@DisplayName("GreeterService 集成测试")
public class GreeterServiceTest {
    
    private static TestConfigManager configManager;
    
    @BeforeAll
    static void setUp() {
        configManager = new TestConfigManager();
        configManager.init();
    }
    
    /**
     * HTTP 模式测试 - 通过 Higress 网关调用
     * 
     * 【重要说明】greet 接口返回的是纯文本字符串，不是 JSON
     * 例如："Hello World, from 172.18.0.4:37768 (to 172.18.0.2:50051)"
     */
    @Test
    @DisplayName("HTTP 模式 - Greet 测试")
    void testGreetHttpMode() throws Exception {
        // 只在 HTTP 模式下执行
        org.junit.jupiter.api.Assumptions.assumeTrue(configManager.isHttpMode());
        
        HttpClientUtils httpClient = new HttpClientUtils(configManager.getHigressUrl());
        
        // 调用服务
        String response = httpClient.greet("World");
        
        // 验证响应
        assertNotNull(response);
        System.out.println("HTTP 模式响应：" + response);
        
        // greet 接口返回纯文本字符串，直接验证是否包含 "World"
        assertTrue(response.contains("World"), "响应应该包含 'World'");
        System.out.println("✓ HTTP 模式测试通过：" + response);
    }
    
    /**
     * Dubbo RPC 模式测试 - 直接调用 Dubbo 服务
     */
    @Test
    @DisplayName("Dubbo RPC 模式 - Greet 测试")
    void testGreetDubboMode() {
        // 只在 Dubbo RPC 模式下执行
        org.junit.jupiter.api.Assumptions.assumeTrue(configManager.isDubboMode());
        
        var greeterService = configManager.getGreeterService();
        assertNotNull(greeterService);
        
        // 调用服务
        var response = greeterService.greet("Dubbo RPC World");
        assertNotNull(response);
        
        System.out.println("Dubbo RPC 模式响应：" + response);
        assertTrue(response.contains("Dubbo RPC World"));
        System.out.println("✓ Dubbo RPC 模式测试通过：" + response);
    }
}
