#!/bin/bash

sudo docker ps -a

sudo docker stop chat-app-cluster_consumer-service0_1
sudo docker stop chat-app-cluster_consumer-service1_1
sudo docker stop chat-app-cluster_producer-service0_1
sudo docker stop chat-app-cluster_producer-service1_1
sudo docker stop chat-app-cluster_kafka1_1
sudo docker stop chat-app-cluster_kafka2_1
sudo docker stop chat-app-cluster_zookeeper1_1
sudo docker stop chat-app-cluster_zookeeper2_1

sudo docker rm chat-app-cluster_consumer-service0_1
sudo docker rm chat-app-cluster_consumer-service1_1
sudo docker rm chat-app-cluster_producer-service0_1
sudo docker rm chat-app-cluster_producer-service1_1
sudo docker rm chat-app-cluster_kafka1_1
sudo docker rm chat-app-cluster_kafka2_1
sudo docker rm chat-app-cluster_zookeeper1_1
sudo docker rm chat-app-cluster_zookeeper2_1

sudo docker ps -a
