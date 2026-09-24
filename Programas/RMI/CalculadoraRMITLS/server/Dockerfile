# Criar a imagem docker (a partir do diretório raiz do projeto)
# $ sudo docker build -t calculator-rmi-server -f server/Dockerfile .

# Executar a imagem:
# $ sudo docker run -d -p 1099:1099 --name rmi-server calculator-rmi

# Obter o endereço IP da imagem:
# $ sudo docker inspect rmi-server | grep -i ipaddress

# Usar a imagem OpenJDK para rodar o servidor
FROM eclipse-temurin:11-jre

# Definir o diretório de trabalho no contêiner
WORKDIR /app

# Copiar os JARs gerados do estágio de build para o diretório de trabalho
COPY ../target/rmi-server.jar /app/rmi-server.jar
COPY ./server/security/server-keystore.p12 /app/security/server-keystore.p12

# Expor a porta necessária para o servidor RMI
EXPOSE 1099

# Iniciar o servidor RMI
#CMD ["java", "-jar", "rmi-server.jar"]
CMD [ \
    "java", \
    "-Djavax.net.ssl.keyStore=/app/security/server-keystore.p12", \
    "-Djavax.net.ssl.keyStorePassword=changeit", \
    "-Djavax.net.ssl.keyStoreType=PKCS12", \
    "-Djavax.net.debug=ssl,handshake", \
    "-jar", \
    "rmi-server.jar" \
]