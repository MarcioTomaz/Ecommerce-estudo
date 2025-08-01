
FROM eclipse-temurin:17-jdk-focal as build

WORKDIR /app

COPY pom.xml .

RUN ./mvnw dependency:go-offline -Dmaven.test.skip=true

COPY src ./src

RUN ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]