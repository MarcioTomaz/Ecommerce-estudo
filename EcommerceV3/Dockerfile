FROM maven:3.9.9-eclipse-temurin-25 AS build

WORKDIR /app

# Copia apenas o pom para resolver dependências em cache
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Copia o código e constrói o projeto
COPY src ./src
RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
