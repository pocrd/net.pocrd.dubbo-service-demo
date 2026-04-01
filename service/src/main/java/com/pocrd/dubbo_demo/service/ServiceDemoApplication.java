package com.pocrd.dubbo_demo.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Dubbo Triple Service Application
 * This application exposes services via Dubbo Triple protocol
 * and registers with Nacos service discovery
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.pocrd.dubbo_demo.dao.autogen.mapper")
public class ServiceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDemoApplication.class, args);
    }
}