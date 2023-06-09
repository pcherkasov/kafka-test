FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
COPY src ./src
RUN mvn package

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/kafka-producer-1-exec.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
