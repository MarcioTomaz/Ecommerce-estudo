# Estágio de construção
FROM eclipse-temurin:21-jdk-jammy as builder

# Define o diretório de trabalho dentro do contêiner
# Este WORKDIR é onde os arquivos serão copiados DENTRO do container.
WORKDIR /app

# Copia o wrapper Maven e o pom.xml
# ATENÇÃO: Os caminhos de origem agora incluem 'EcommerceV3/'
COPY EcommerceV3/.mvn/ .mvn/
COPY EcommerceV3/mvnw EcommerceV3/pom.xml ./

# Garante que o script mvnw seja executável
RUN chmod +x mvnw

# Baixa as dependências do Maven. Isso economiza tempo em builds subsequentes
# se apenas o código-fonte mudar.
RUN ./mvnw dependency:go-offline -B

# Copia todo o código-fonte do projeto
# ATENÇÃO: O caminho de origem agora inclui 'EcommerceV3/src'
COPY EcommerceV3/src ./src

# Empacota a aplicação Spring Boot em um JAR executável
# O comando clean package -DskipTests irá limpar, empacotar e pular os testes
RUN ./mvnw clean package -DskipTests

# Estágio de execução
# Usamos uma imagem JRE menor para o tempo de execução
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Expõe a porta que a aplicação Spring Boot irá escutar
EXPOSE 8080

# Copia o JAR empacotado do estágio de construção para o estágio de execução
# O nome do JAR pode variar. Você precisará verificar o nome exato gerado pelo Maven.
# Geralmente, é algo como target/<nome-do-projeto>-<versao>.jar
# Você pode inspecionar o log da construção ou a pasta 'target' localmente para confirmar.
COPY --from=builder /app/target/*.jar app.jar

# Define o ponto de entrada para a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

# Variável de ambiente para o Render usar a porta correta
ENV PORT 8080
