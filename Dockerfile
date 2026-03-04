FROM eclipse-temurin:21

WORKDIR /app

# 复制本地编译的 fat jar
# 外部依赖已在 Maven 阶段缓存（~/.m2/repository），这里直接复制打包好的 fat jar
COPY service/target/service-demo-service-1.0.0.jar app.jar

# 暴露 Dubbo Triple 协议端口
EXPOSE 50051

# 使用标准 jar 方式启动
ENTRYPOINT ["java", "-jar", "app.jar"]
