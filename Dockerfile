# Etapa 1: Construir a aplicação
FROM gradle:8.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Etapa 2: Executar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
