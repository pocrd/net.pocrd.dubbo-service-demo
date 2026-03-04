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
        
        // 解析 JSON 响应
        var jsonNode = httpClient.parseJson(response);
        assertTrue(jsonNode.has("message") || jsonNode.has("greeting"));
        
        String message = jsonNode.has("message") ? 
            jsonNode.get("message").asText() : 
            jsonNode.get("greeting").asText();
        assertTrue(message.contains("World"));
        System.out.println("✓ HTTP 模式测试通过：" + message);
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
