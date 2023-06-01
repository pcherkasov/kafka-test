FROM openjdk:17-alpine
LABEL authors="pavelc"

COPY .mvn .mvn
COPY pom.xml \
     mvnw \
     ./
COPY . ./
RUN chmod +x mvnw
RUN ./mvnw -DskipTests=true package
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/kafka-producer-1-exec.jar"]
