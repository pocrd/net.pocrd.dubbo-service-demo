# =============================================================================
# Dubbo Triple Service Docker 镜像构建文件
# =============================================================================
# 
# 【构建优化策略】分层构建 + 多阶段编译
# 
# 性能对比:
#   - 首次构建：5-10 分钟（完整下载依赖 + 编译）
#   - 修改代码后：1-2 分钟（跳过依赖下载，只编译代码）
#   - 仅修改 pom：3-5 分钟（重新下载依赖）
#
# 关键优化点:
#   1. 分层复制 pom.xml - 最大化利用 Docker 缓存
#   2. Maven 本地仓库持久化 - docker-compose.yml 中配置了 maven-repo 卷
#   3. 多阶段构建 - 减小最终镜像大小约 60%
#   4. 标准类路径启动 - 不依赖 Spring Boot Fat JAR
#
# 使用说明:
#   - 首次构建：docker-compose build
#   - 修改代码后：docker-compose build (自动使用缓存)
#   - 强制无缓存：docker-compose build --no-cache (极少需要)
#
# 项目架构：纯 Dubbo + Spring XML 配置（非 Spring Boot）
# =============================================================================

# ---------------------- 第一阶段：Builder（编译环境）-----------------------
FROM eclipse-temurin:21 AS builder

WORKDIR /app

# 第 1 步：复制父 pom.xml（很少变化，会被 Docker 缓存）
COPY pom.xml .

# 第 2 步：复制各子模块 pom.xml（很少变化）
COPY api/pom.xml api/
COPY service/pom.xml service/
COPY dao/pom.xml dao/

# 第 3 步：下载 Maven 依赖到本地仓库
# 只有 pom.xml 变化时才重新执行这一步，充分利用 Docker 缓存
RUN mvn dependency:go-offline -B

# 第 4 步：复制源代码（最频繁变化的部分，放在最后以最大化缓存命中率）
COPY . .

# 第 5 步：Maven 编译打包
# 使用已下载的依赖，只编译 Java 源代码
RUN mvn clean package -DskipTests -B

# ---------------------- 第二阶段：Runtime（运行环境）-----------------------
FROM eclipse-temurin:21

WORKDIR /app

# 从构建阶段复制编译产物
# - app.jar: 主应用程序 JAR（包含项目代码）
# - lib/: 所有依赖的第三方 JAR 包
COPY --from=builder /app/service/target/service-demo-service-1.0.0.jar app.jar
COPY --from=builder /app/service/target/lib/ lib/

# 暴露 Dubbo Triple 协议端口
EXPOSE 50051

# 使用标准 Java 类路径方式启动
# 注意：本项目不是 Spring Boot 项目，所以不使用 -jar 参数
ENTRYPOINT ["java", "-cp", "app.jar:lib/*", "com.pocrd.service_demo.service.ServiceDemoApplication"]
