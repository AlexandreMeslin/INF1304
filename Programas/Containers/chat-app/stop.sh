#!/bin/bash

sudo docker ps -a

sudo docker stop chat-app-consumer-service-1
sudo docker stop chat-app-producer-service-1
sudo docker stop chat-app-kafka-1
sudo docker stop chat-app-zookeeper-1

sudo docker rm chat-app-consumer-service-1
sudo docker rm chat-app-producer-service-1
sudo docker rm chat-app-kafka-1
sudo docker rm chat-app-zookeeper-1

sudo docker ps -a