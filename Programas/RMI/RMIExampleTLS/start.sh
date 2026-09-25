#!/bin/bash

sudo docker rmi rmitls-server:latest -f
sudo docker rmi rmitls-client:latest -f
mvn clean install && \
sudo docker compose up && \
sudo docker compose down
