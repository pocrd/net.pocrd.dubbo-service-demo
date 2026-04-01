package com.pocrd.dubbo_demo.client;

import com.pocrd.dubbo_demo.api.GreeterServiceInternal;
import com.pocrd.dubbo_demo.api.entity.ServiceInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * GreeterServiceInternal 内部接口测试
 * 
 * 测试 GreeterServiceInternal 接口的所有方法
 * 仅支持 Dubbo RPC 模式，因为内部接口不通过 Higress 暴露
 */
@DisplayName("GreeterServiceInternal 内部接口测试")
public class GreeterServiceInternalTest {
    
    private static TestConfigManager configManager;
    private static GreeterServiceInternal internalService;
    
    @BeforeAll
    static void setUp() {
        configManager = new TestConfigManager();
        configManager.init();
        
        // 获取内部服务接口
        internalService = configManager.getGreeterServiceInternal();
    }
    
    /**
     * 测试内部问候接口
     */
    @Test
    @DisplayName("Dubbo RPC 模式 - greetInternal 测试")
    void testGreetInternal() {
        assertNotNull(internalService);
        
        String name = "InternalUser";
        String response = internalService.greetInternal(name);
        
        assertNotNull(response);
        assertTrue(response.contains("[Internal]"));
        assertTrue(response.contains(name));
        
        System.out.println("greetInternal 响应: " + response);
        System.out.println("✅ greetInternal 测试通过");
    }
    
    /**
     * 测试批量问候接口
     */
    @Test
    @DisplayName("Dubbo RPC 模式 - greetBatch 测试")
    void testGreetBatch() {
        assertNotNull(internalService);
        
        List<String> names = Arrays.asList("User1", "User2", "User3");
        List<String> responses = internalService.greetBatch(names);
        
        assertNotNull(responses);
        assertEquals(3, responses.size());
        
        for (int i = 0; i < names.size(); i++) {
            String response = responses.get(i);
            assertTrue(response.contains("[Internal Batch]"));
            assertTrue(response.contains(names.get(i)));
            System.out.println("greetBatch 响应 " + (i + 1) + ": " + response);
        }
        
        System.out.println("✅ greetBatch 测试通过");
    }
    
    /**
     * 测试健康检查接口
     */
    @Test
    @DisplayName("Dubbo RPC 模式 - healthCheck 测试")
    void testHealthCheck() {
        assertNotNull(internalService);
        
        boolean isHealthy = internalService.healthCheck();
        
        assertTrue(isHealthy, "服务健康检查应返回 true");
        
        System.out.println("healthCheck 结果: " + isHealthy);
        System.out.println("✅ healthCheck 测试通过");
    }
    
    /**
     * 测试服务信息获取接口
     */
    @Test
    @DisplayName("Dubbo RPC 模式 - getServiceInfo 测试")
    void testGetServiceInfo() {
        assertNotNull(internalService);
        
        ServiceInfo serviceInfo = internalService.getServiceInfo();
        
        assertNotNull(serviceInfo);
        assertNotNull(serviceInfo.serviceName());
        assertNotNull(serviceInfo.version());
        assertTrue(serviceInfo.uptime() >= 0);
        assertTrue(serviceInfo.requestCount() >= 0);
        
        System.out.println("ServiceInfo:");
        System.out.println("  服务名: " + serviceInfo.serviceName());
        System.out.println("  版本: " + serviceInfo.version());
        System.out.println("  运行时间: " + serviceInfo.uptime() + "ms");
        System.out.println("  请求数: " + serviceInfo.requestCount());
        System.out.println("✅ getServiceInfo 测试通过");
    }
}
