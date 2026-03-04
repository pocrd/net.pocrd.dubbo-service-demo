package com.pocrd.service_demo.client;

import com.pocrd.service_demo.api.GreeterServiceStreamInternal;
import org.apache.dubbo.common.stream.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * GreeterService 流式接口测试
 * 
 * 测试双向流式 (Bidirectional Streaming) 接口 greetInteractive
 * 该接口定义在 GreeterServiceInternal 中，仅用于内部服务间调用
 * 
 * 【重要说明】
 * 双向流式接口不适合通过 Higress 网关暴露给外部，因为：
 * - HTTP/2 模式下客户端只能发送一个请求，无法真正实现双向流式
 * - 真正的双向流式需要 Dubbo RPC 客户端支持 StreamObserver
 * 
 * 因此 greetInteractive 接口只在 GreeterServiceInternal 中定义，供内部服务使用
 */
@DisplayName("GreeterService 流式接口测试")
public class GreeterServiceStreamingTest {
    
    private static TestConfigManager configManager;
    private static GreeterServiceStreamInternal streamService;
    
    @BeforeAll
    static void setUp() {
        // 流式接口使用 Triple 协议（基于 gRPC，天然支持流式）
        System.setProperty("dubbo.url", "tri://localhost:50051");
        configManager = new TestConfigManager();
        configManager.init();
        
        // 获取流式服务接口
        streamService = configManager.getGreeterServiceStreamInternal();
    }
    
    /**
     * 双向流式测试 - greetInteractive
     * 客户端发送多个名字，服务端实时返回问候语
     */
    @Test
    @DisplayName("Dubbo RPC 模式 - 双向流式交互测试")
    void testGreetInteractive() throws Exception {
        assertNotNull(streamService);
        
        // 用于存储服务端返回的消息
        List<String> responses = new ArrayList<>();
        
        // 用于等待流式调用完成
        CountDownLatch latch = new CountDownLatch(1);
        
        // 创建响应观察者（接收服务端返回的消息）
        StreamObserver<String> responseObserver = new StreamObserver<String>() {
            @Override
            public void onNext(String response) {
                responses.add(response);
                System.out.println("收到服务端响应: " + response);
            }
            
            @Override
            public void onError(Throwable throwable) {
                System.err.println("流式调用出错: " + throwable.getMessage());
                latch.countDown();
            }
            
            @Override
            public void onCompleted() {
                System.out.println("服务端流式调用完成");
                latch.countDown();
            }
        };
        
        // 获取请求观察者（用于向服务端发送消息）
        StreamObserver<String> requestObserver = streamService.greetInteractive(responseObserver);
        
        // 发送多个名字到服务端
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve"};
        System.out.println("开始双向流式交互测试...");
        System.out.println("发送 " + names.length + " 个名字到服务端");
        
        for (String name : names) {
            System.out.println("客户端发送: " + name);
            requestObserver.onNext(name);
            // 稍微延迟，模拟真实交互场景
            Thread.sleep(100);
        }
        
        // 通知服务端客户端发送完成
        requestObserver.onCompleted();
        
        // 等待服务端处理完成（最多 10 秒）
        boolean completed = latch.await(10, TimeUnit.SECONDS);
        assertTrue(completed, "流式调用未在预期时间内完成");
        
        // 验证结果
        System.out.println("\n验证结果:");
        System.out.println("期望收到 " + names.length + " 条响应");
        System.out.println("实际收到 " + responses.size() + " 条响应");
        
        assertEquals(names.length, responses.size(), 
            "期望收到 " + names.length + " 条响应，实际收到 " + responses.size());
        
        // 验证每条响应都包含对应的名字
        for (int i = 0; i < names.length; i++) {
            String response = responses.get(i);
            String name = names[i];
            assertTrue(response.contains(name), 
                "第 " + (i + 1) + " 条响应应包含名字 '" + name + "'，实际响应: " + response);
            System.out.println("✓ 响应 " + (i + 1) + " 验证通过: 包含 '" + name + "'");
        }
        
        System.out.println("\n✅ 双向流式交互测试通过!");
    }
}
