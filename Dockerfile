FROM registry.access.redhat.com/ubi8/openjdk-17:latest as build
USER root
COPY . /tmp/src
WORKDIR /tmp/src
RUN mvn clean package -DskipTests

FROM registry.access.redhat.com/ubi8/openjdk-17:latest
# Importante: El nombre del JAR debe coincidir con el de tu pom.xml
COPY --from=build /tmp/src/target/coin-toss-api-1.0.0-SNAPSHOT.jar /deployments/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/deployments/app.jar"]
