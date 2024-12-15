# 使用官方的 Java 镜像作为基础镜像
FROM openjdk:17-jdk-slim

# 安装必要的依赖，包括 libglib 和其他缺少的库
RUN apt-get update && apt-get install -y \
    libglib2.0-0 \
    libx11-dev \
    libgdk-pixbuf2.0-dev \
    libgtk-3-0 \
    libnss3 \
    libasound2 \
    libxcomposite1 \
    libxrandr2 \
    libxi6 \
    libxtst6 \
    && rm -rf /var/lib/apt/lists/*

# 如果需要安装 msedgedriver
RUN apt-get install -y wget
RUN wget https://msedgedriver.azureedge.net/131.0.2903.87/edgedriver_linux64.zip
RUN unzip edgedriver_linux64.zip
RUN mv msedgedriver /usr/local/bin

# 设置工作目录
WORKDIR /app

# 复制 Maven 构建的 JAR 文件到容器中
COPY target/PriceComWebBackend-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8082

# 运行应用程序
ENTRYPOINT ["java", "-jar", "app.jar"]