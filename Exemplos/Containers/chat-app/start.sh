#!/bin/bash

# Função para verificar o resultado de um comando e abortar o script se houver um erro
check_result() {
    local resultado=$1
    if [[ $resultado -ne 0 ]]; then
        exit $resultado
    fi
#    echo -n "Ready ==> "
#    read
    echo
    echo
    echo
}

./stop.sh

# Produtor
echo Criando o produtor
cd producer-service/
mvn clean package
check_result $?

# Consumidor
echo Criando o consumidor
cd ../consumer-service/
mvn clean package
check_result $?

# Containers
echo Criando os containers
cd ..
sudo docker-compose build
check_result $?

echo Subindo os containers
sudo docker-compose up -d
check_result $?

sudo docker ps -a
cd frontend/
#python3 -m http.server --bind 0.0.0.0 8000