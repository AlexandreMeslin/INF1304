#!/bin/bash

sudo docker rmi rmi-server:latest -f
sudo docker rmi rmi-client:latest -f && \
mvn clean install && \
sudo docker compose up && \
sudo docker compose down
