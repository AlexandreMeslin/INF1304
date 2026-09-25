#!/bin/bash

rm -f ./server/security/server-keystore.p12
rm -f ./server/security/rmitls-server.crt
rm -f ./client/security/client-truststore.p12

# Este script gera certificados autoassinados para o servidor RMI com TLS.
# Para gerar o certificado autoassinado, utilize o seguinte comando:
keytool -genkeypair \
   -alias rmitls-server \
   -keyalg RSA \
   -keysize 2048 \
   -validity 365 \
   -keystore ./server/security/server-keystore.p12 \
   -storetype PKCS12 \
   -storepass changeit \
   -keypass changeit \
   -dname "CN=rmi-server, OU=INF1304, O=PUC-Rio, L=Rio de Janeiro, ST=RJ, C=BR" \
   -ext "SAN=dns:rmi-server,dns:rmitls-server"
 

keytool -exportcert \
   -alias rmitls-server \
   -keystore ./server/security/server-keystore.p12 \
   -storetype PKCS12 \
   -storepass changeit \
   -rfc \
   -file ./server/security/rmitls-server.crt

# Para importar o certificado do servidor no cliente, utilize o seguinte comando:
keytool -importcert \
   -alias rmitls-server \
   -file ./server/security/rmitls-server.crt \
   -keystore ./client/security/client-truststore.p12 \
   -storetype PKCS12 \
   -storepass changeit \
   -noprompt
