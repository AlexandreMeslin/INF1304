#!/bin/bash
# Script para reconstruir a imagem do web e atualizar o deployment no Kubernetes

echo "🚀 Iniciando o processo de reconstrução e atualização do Kubernetes..."

# Passo 1: Construir a nova imagem do web
echo "🛠️ Construindo a nova imagem do web..."
eval $(minikube docker-env)
docker build -t factoryflow-web:v2 -f web/Dockerfile ./web || exit 1

# Passo 2: Atualizar o deployment no Kubernetes
echo "🔄 Atualizando o deployment do web..."
kubectl apply -f k8s/namespace.yaml || exit 1
kubectl apply -f k8s/mysql.yaml || exit 1
kubectl apply -f k8s/rabbitmq.yaml || exit 1
kubectl apply -f k8s/web.yaml || exit 1
kubectl rollout restart deployment web-deployment -n factoryflow || exit 1
kubectl get pods -n factoryflow || exit 1
kubectl get service -n factoryflow || exit 1
minikube addons enable metrics-server || exit 1
kubectl apply -f k8s/hpa.yaml || exit 1

# Instalar Prometheus e Grafana para monitoramento
echo "📊 Instalando Prometheus e Grafana para monitoramento..."
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts || exit 1
helm repo update || exit 1
helm install monitoring prometheus-community/kube-prometheus-stack --namespace monitoring --create-namespace || exit 1

echo "✅ Atualização concluída."
echo "🔍 Verifique os pods e serviços para garantir que tudo esteja funcionando corretamente."
