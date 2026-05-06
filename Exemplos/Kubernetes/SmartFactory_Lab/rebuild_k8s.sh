#!/bin/bash
# Script para reconstruir a imagem do web e atualizar o deployment no Kubernetes

echo "🚀 Iniciando o processo de reconstrução e atualização do Kubernetes..."

# Passo 1: Construir a nova imagem do web
echo "🛠️ Construindo a nova imagem do web..."
eval $(minikube docker-env)
docker build -t factoryflow-web:v2 -f web/Dockerfile ./web

# Passo 2: Atualizar o deployment no Kubernetes
echo "🔄 Atualizando o deployment do web..."
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/mysql.yaml
kubectl apply -f k8s/rabbitmq.yaml
kubectl apply -f k8s/web.yaml
kubectl rollout restart deployment web-deployment -n factoryflow
kubectl get pods -n factoryflow
kubectl get service -n factoryflow

echo "✅ Atualização concluída."
echo "🔍 Verifique os pods e serviços para garantir que tudo esteja funcionando corretamente."
