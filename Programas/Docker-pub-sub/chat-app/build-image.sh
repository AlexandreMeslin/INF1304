#!/bin/bash

# Função para verificar o resultado de um comando e abortar o script se houver um erro
check_result() {
    local resultado=$1
    if [[ $resultado -ne 0 ]]; then
        exit $resultado
    fi
    echo
}

cd producer-service/
mvn clean package
check_result $?
cd ..
docker build -t producer-service:latest ./producer-service

cd consumer-service/
mvn clean package
check_result $?
cd ..
docker build -t consumer-service:latest ./consumer-service