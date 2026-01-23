# ==========================
# Build stage
# ==========================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# 先只複製 pom.xml，用來快取相依套件
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# 再複製原始碼
COPY src ./src

# 打包成 jar（略過測試）
RUN mvn -B clean package -DskipTests

# ==========================
# Runtime stage
# ==========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 建一個非 root user
RUN addgroup -g 1001 -S appgroup \
    && adduser -u 1001 -S appuser -G appgroup

# 從 builder 拿 jar
COPY --from=builder /app/target/*.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

# JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# Gateway 用 8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]