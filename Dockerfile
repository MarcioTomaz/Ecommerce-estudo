# Etapa 1: Build com Maven
FROM maven:3.8.5-openjdk-17 AS build

# Diretório de trabalho será /app
WORKDIR /app

# Copia o projeto inteiro da pasta EcommerceV3
COPY EcommerceV3/ .

# Compila o projeto (sem testes)
RUN mvn clean package -DskipTests

# Etapa 2: Executar o JAR com imagem leve
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia o JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
