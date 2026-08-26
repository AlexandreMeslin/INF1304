#!/bin/bash

sudo docker ps -a

sudo docker stop chat-app-cluster-kafka-1-1
sudo docker stop chat-app-cluster-kafka-2-1
sudo docker stop chat-app-cluster-kafka-3-1
sudo docker stop chat-app-cluster-producer-service-1
sudo docker stop chat-app-cluster-consumer-service-1

sudo docker rm chat-app-cluster-consumer-service-1
sudo docker rm chat-app-cluster-producer-service-1
sudo docker rm chat-app-cluster-kafka-1-1
sudo docker rm chat-app-cluster-kafka-2-1
sudo docker rm chat-app-cluster-kafka-3-1

sudo docker ps -a