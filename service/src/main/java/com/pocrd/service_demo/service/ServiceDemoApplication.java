package com.pocrd.service_demo.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Dubbo Triple Service Application
 * This application exposes services via Dubbo Triple protocol
 * and registers with Nacos service discovery
 */
@SpringBootApplication
@EnableDubbo
public class ServiceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDemoApplication.class, args);
    }
}