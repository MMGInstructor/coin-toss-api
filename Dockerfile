# Etapa 1: Compilación con Maven
FROM maven:3.8.4-openjdk-17-slim AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución ligera
FROM registry.access.redhat.com/ubi8/openjdk-17-runtime:latest
USER 185
WORKDIR /deployments
# Copiamos el JAR que genera el plugin de Spring Boot
COPY --from=build /app/target/coin-toss-api-1.0.0-SNAPSHOT.jar /deployments/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/deployments/app.jar"]
