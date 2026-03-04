FROM eclipse-temurin:21

WORKDIR /app

# 复制宿主机编译产物（需要先在宿主机执行 mvn clean package）
COPY service/target/service-demo-service-1.0.0.jar app.jar
COPY service/target/lib/ lib/

# 暴露 Dubbo Triple 协议端口
EXPOSE 50051

# 使用标准 Java 类路径方式启动
# 注意：本项目不是 Spring Boot 项目，所以不使用 -jar 参数
ENTRYPOINT ["java", "-cp", "app.jar:lib/*", "com.pocrd.service_demo.service.ServiceDemoApplication"]
