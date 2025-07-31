# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia a pasta do projeto para dentro da imagem
COPY EcommerceV3 /app/EcommerceV3

# Vai até a pasta onde está o pom.xml e compila
WORKDIR /app/EcommerceV3
RUN mvn clean package -DskipTests

# Etapa 2: imagem final leve
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o .jar compilado da etapa anterior
COPY --from=build /app/EcommerceV3/target/*.jar app.jar

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
