# Etapa 1: build com Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copia apenas a pasta do projeto
COPY EcommerceV3 /app

# Executa o build
RUN mvn clean package -DskipTests

# Etapa 2: imagem leve para rodar o app
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
