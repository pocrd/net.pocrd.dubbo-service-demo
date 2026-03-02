FROM registry.cn-hangzhou.aliyuncs.com/public/dragonwell:21-jdk-anolis

# 设置工作目录
WORKDIR /app

# 复制整个项目源码到容器中
COPY . .

# 使用Maven编译整个多模块项目
RUN mvn clean package -DskipTests

# 选择service模块的jar包作为启动目标
# 假设service模块的artifactId是 'service'
ENTRYPOINT ["java", "-cp", "/app/service/target/classes:/app/service/target/lib/*", "com.pocrd.service_demo.service.DubboTripleServiceApplication"]
