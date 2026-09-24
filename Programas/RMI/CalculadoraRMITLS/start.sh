#!/bin/bash

mvn clean install && \
sudo docker rmi calculator-rmi-server:latest -f && \
sudo docker rmi calculator-rmi-client:latest -f && \
sudo docker compose up && \
sudo docker compose down
