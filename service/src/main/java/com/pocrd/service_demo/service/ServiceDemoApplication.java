package com.pocrd.service_demo.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Dubbo Triple Service Application
 * This application exposes services via Dubbo Triple protocol
 * and registers with Nacos service discovery
 */
@EnableDubbo
public class ServiceDemoApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceDemoApplication.class);
    
    public static void main(String[] args) throws Exception {
        logger.info("Starting Dubbo Triple Service Application...");
        
        // 使用Dubbo标准的Spring上下文启动方式
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-provider.xml");
        context.start();
        
        logger.info("Dubbo Triple Service is running successfully!");
        logger.info("Service registered with Nacos and listening on port 50051 (Triple protocol)");
        logger.info("Press Ctrl+C to stop the service...");
        
        // 添加关闭钩子，优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down Dubbo service...");
            context.close();
            logger.info("Dubbo service stopped.");
        }));
        
        // 保持应用运行
        Thread.currentThread().join();
    }
}