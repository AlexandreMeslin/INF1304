# Criar a imagem docker (a partir do diretório raiz do projeto)
# $ sudo docker build -t calculator-rmi-client -f client/Dockerfile .

# Executar a imagem:
# $ sudo docker run -d -p 1099:1099 --name rmi-client calculator-rmi-client

# Obter o endereço IP da imagem:
# $ sudo docker inspect rmi-client | grep -i ipaddress

# Usar a imagem OpenJDK para rodar o cliente
FROM openjdk:11-jre-slim

# Definir o diretório de trabalho no contêiner
WORKDIR /app

# Copiar os JARs gerados do estágio de build para o diretório de trabalho
COPY ../target/rmi-client.jar /app/rmi-client.jar

# Iniciar o cliente RMI
CMD ["java", "-jar", "rmi-client.jar"]
