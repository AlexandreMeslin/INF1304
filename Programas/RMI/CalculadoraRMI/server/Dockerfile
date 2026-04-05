# Criar a imagem docker (a partir do diretório raiz do projeto)
# $ sudo docker build -t calculator-rmi-server -f server/Dockerfile .

# Executar a imagem:
# $ sudo docker run -d -p 1099:1099 --name rmi-server calculator-rmi

# Obter o endereço IP da imagem:
# $ sudo docker inspect rmi-server | grep -i ipaddress

# Usar a imagem OpenJDK para rodar o servidor
FROM openjdk:11-jre-slim

# Definir o diretório de trabalho no contêiner
WORKDIR /app

# Copiar os JARs gerados do estágio de build para o diretório de trabalho
COPY ../target/rmi-server.jar /app/rmi-server.jar

# Expor a porta necessária para o servidor RMI
EXPOSE 1099

# Iniciar o servidor RMI
CMD ["java", "-jar", "rmi-server.jar"]
