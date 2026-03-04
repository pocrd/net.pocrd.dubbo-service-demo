package com.pocrd.service_demo.api.entity;

/**
 * 服务信息数据记录
 * 
 * 使用 JDK 16+ Record 特性，自动生成：
 * - 构造函数
 * - 访问器方法（serviceName(), version(), uptime(), requestCount()）
 * - equals() 和 hashCode()
 * - toString()
 */
public record ServiceInfo(
    String serviceName,
    String version,
    long uptime,
    int requestCount
) {}
