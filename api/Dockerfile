FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package  # Executa o objetivo package

FROM openjdk:17-alpine

# Instalar fontes, se necessário
RUN apk update && apk add --no-cache fontconfig ttf-dejavu

# Copiar o JAR da aplicação
COPY --from=build /app/target/evibbra-1.0.0.jar /evibbra.jar

# Expor a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação com as variáveis de ambiente
CMD ["java", "-jar", "/evibbra.jar"]