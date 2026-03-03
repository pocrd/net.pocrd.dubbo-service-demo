FROM eclipse-temurin:21

# 安装Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

# 设置工作目录
WORKDIR /app

# 复制项目文件
COPY . .

# 使用Maven编译整个多模块项目
RUN mvn clean package -DskipTests

# 选择service模块的jar包作为启动目标
ENTRYPOINT ["java", "-jar", "/app/service/target/service-demo-service-1.0.0.jar"]
