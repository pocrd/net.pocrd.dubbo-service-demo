# 服务接口重构说明

## 📋 重构概述

本次重构将原有的单一 `GreeterService` 拆分为两个独立的接口，分别服务于不同的场景：

1. **GreeterServiceHttpExport** - 对外 HTTP 接口（通过 Higress 网关暴露）
2. **GreeterServiceInternal** - 对内 Dubbo RPC 接口（仅内部服务调用）

## 🏗️ 接口架构

```
┌─────────────────────────────────────────────────────┐
│                 API Module (接口层)                  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  GreeterServiceHttpExport          GreeterServiceInternal │
│  ┌──────────────────────┐         ┌──────────────────────┐
│  │ • greet()            │         │ • greetInternal()    │
│  │ • greetStream()      │         │ • greetBatch()       │
│  │ • greetInteractive() │         │ • healthCheck()      │
│  │                      │         │ • getServiceInfo()   │
│  └──────────────────────┘         └──────────────────────┘
│           ↓                                  ↓
│  公网 HTTP 访问                        内网 Dubbo RPC 访问
│  (Higress Gateway)                   (Dubbo Native Protocol)
└─────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────┐
│              Service Module (实现层)                 │
├─────────────────────────────────────────────────────┤
│                                                     │
│  GreeterServiceHttpExportImpl     GreeterServiceInternalImpl  │
│  ┌──────────────────────┐         ┌──────────────────────┐
│  │ @DubboService        │         │ @DubboService        │
│  │ version="1.0.0"      │         │ version="1.0.0"      │
│  │ group="default"      │         │ group="internal"     │
│  └──────────────────────┘         └──────────────────────┘
└─────────────────────────────────────────────────────┘
```

## 📦 文件清单

### API 模块 (`api/src/main/java/com/pocrd/service_demo/api/`)

| 文件 | 说明 | 访问方式 |
|------|------|----------|
| `GreeterServiceHttpExport.java` | 对外 HTTP 接口定义 | Higress 网关 → HTTP |
| `GreeterServiceInternal.java` | 对内 Dubbo RPC 接口定义 | Dubbo 原生 RPC |

### Service 模块 (`service/src/main/java/com/pocrd/service_demo/service/impl/`)

| 文件 | 说明 | Dubbo Group |
|------|------|-------------|
| `GreeterServiceHttpExportImpl.java` | HTTP 接口实现 | `default` |
| `GreeterServiceInternalImpl.java` | 内部 RPC 接口实现 | `internal` |

## 🔍 接口详细对比

### GreeterServiceHttpExport（对外 HTTP 接口）

**方法列表**：
- `String greet(String name)` - 简单问候
- `void greetStream(String name, StreamObserver<String> observer)` - 流式问候
- `StreamObserver<String> greetInteractive(StreamObserver<String> observer)` - 交互式问候

**特点**：
- ✅ 通过 Higress 网关暴露给公网
- ✅ 使用 Triple 协议（兼容 gRPC/HTTP2）
- ✅ 支持 RESTful 风格调用
- ✅ 适合移动端、Web 前端访问

**调用示例**：
```bash
# 通过 Higress 网关 HTTP 调用
curl -X POST http://localhost:80/GreeterServiceHttpExport/greet \
  -H "Content-Type: application/json" \
  -d '{"name": "World"}'
```

---

### GreeterServiceInternal（对内 Dubbo RPC 接口）

**方法列表**：
- `String greetInternal(String name)` - 内部问候
- `List<String> greetBatch(List<String> names)` - 批量问候
- `boolean healthCheck()` - 健康检查
- `ServiceInfo getServiceInfo()` - 获取服务信息

**特点**：
- ✅ 仅在内部网络使用
- ✅ 使用 Dubbo 原生 RPC 协议
- ✅ 高性能二进制序列化
- ✅ 适合微服务间调用

**调用示例**：
```java
// Dubbo 消费者配置
@DubboReference(group = "internal")
private GreeterServiceInternal internalService;

// 调用内部服务
String response = internalService.greetInternal("OtherService");
List<String> batchResult = internalService.greetBatch(names);
boolean healthy = internalService.healthCheck();
```

---

## 🎯 使用场景

### 场景 1：对外提供公共服务

**选择**: `GreeterServiceHttpExport`

```yaml
# Higress 路由配置
http:
  routes:
    - match:
        uri: /api/greeting/*
      route:
        service: dubbo-demo-service
        dubbo:
          service: GreeterServiceHttpExport
          version: 1.0.0
          group: default
```

### 场景 2：微服务内部通信

**选择**: `GreeterServiceInternal`

```java
// 其他微服务中
@DubboReference(version = "1.0.0", group = "internal")
private GreeterServiceInternal greeterInternal;

public void businessMethod() {
    // 内部服务间高效调用
    boolean healthy = greeterInternal.healthCheck();
    if (healthy) {
        // 执行业务逻辑
    }
}
```

---

## ⚙️ 配置说明

### Dubbo 服务提供者配置

```xml
<!-- spring/dubbo-provider.xml -->
<dubbo:service interface="com.pocrd.service_demo.api.GreeterServiceHttpExport" 
               ref="greeterServiceHttpExport" 
               version="1.0.0" 
               group="default" />

<dubbo:service interface="com.pocrd.service_demo.api.GreeterServiceInternal" 
               ref="greeterServiceInternal" 
               version="1.0.0" 
               group="internal" />
```

### Higress 网关配置（仅暴露 HTTP 接口）

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: dubbo-http-export
spec:
  rules:
  - host: api.example.com
    http:
      paths:
      - path: /GreeterServiceHttpExport/*
        pathType: Prefix
        backend:
          service:
            name: dubbo-demo-service
            port:
              number: 50051
```

---

## 🚀 测试验证

### 测试 HTTP 接口

```bash
# 1. 直接调用本地服务
curl -X POST http://localhost:50051/GreeterServiceHttpExport/greet \
  -H "Content-Type: application/json" \
  -d '{"name": "Test"}'

# 2. 通过 Higress 网关调用
curl -X POST http://localhost:80/GreeterServiceHttpExport/greet \
  -H "Content-Type: application/json" \
  -d '{"name": "Test"}'
```

### 测试内部 RPC 接口

```bash
# 使用 client 子工程进行 Dubbo RPC 测试
mvn test -pl client -Pdubbo-test \
  -Ddubbo.url=tri://localhost:50051 \
  -Dtest.mode=dubbo
```

---

## 📊 性能对比

| 特性 | HTTP 接口 | Internal RPC |
|------|----------|--------------|
| **协议** | HTTP/2 (Triple) | Dubbo Binary |
| **序列化** | JSON/Protobuf | Hessian2/Protobuf |
| **延迟** | ~5ms | ~1ms |
| **吞吐** | 中等 | 高 |
| **适用** | 对外 API | 内部通信 |

---

## ⚠️ 注意事项

1. **不要混用接口**
   - 对外服务使用 `GreeterServiceHttpExport`
   - 对内服务使用 `GreeterServiceInternal`

2. **版本控制**
   - 两个接口独立版本号管理
   - 可以独立升级不互相影响

3. **安全考虑**
   - HTTP 接口需要配置认证、限流等安全措施
   - Internal 接口依赖内网隔离，相对安全

4. **监控告警**
   - 分别为两个接口配置独立的监控指标
   - 设置不同的告警阈值

---

## 🔗 相关文件

- [API 接口定义](../api/src/main/java/com/pocrd/service_demo/api/)
- [服务实现](../service/src/main/java/com/pocrd/service_demo/service/impl/)
- [Client 测试工程](../client/GUIDE.md)
- [Docker 部署配置](../docker-compose.yml)

---

**最后更新**: 2026-03-03  
**版本**: v2.0.0
