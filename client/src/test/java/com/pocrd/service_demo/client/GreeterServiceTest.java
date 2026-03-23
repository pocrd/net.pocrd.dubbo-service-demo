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
 */
@DisplayName("GreeterService 集成测试")
public class GreeterServiceTest {
    
    private static HttpClientUtils httpClient;
    private static TestConfigManager configManager;
    
    @BeforeAll
    static void setUp() {
        // 初始化 HTTP 客户端（连接 Higress 网关，端口 80）
        httpClient = new HttpClientUtils("http://localhost:80");
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
}
