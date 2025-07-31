# Etapa 1: Build com Maven usando Java 21
FROM eclipse-temurin:21-jdk as build

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

RUN mvn clean package -DskipTests

# Etapa 2: Runtime com JDK 21 leve
FROM eclipse-temurin:21-jdk as runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
