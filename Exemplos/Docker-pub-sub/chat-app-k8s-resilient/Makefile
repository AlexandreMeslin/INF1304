NAMESPACE=kafka-demo
YAML_FILES=$(wildcard *.yml)

PRODUCER_IMAGE=producer-service:latest
CONSUMER_IMAGE=consumer-service:latest

.PHONY: all create-ns apply delete status \
		logs-producer logs-consumer logs-kafka logs-zookeeper \
		port-forward-producer port-forward-consumer clean \
		up down logs load-images

all:	apply

create-ns:
	@echo "Criando namespace $(NAMESPACE)..."
	kubectl create namespace $(NAMESPACE) --dry-run=client -o yaml | kubectl apply -f -

apply:	create-ns
	@echo "Aplicando todos os manifests..."
	kubectl apply -n $(NAMESPACE) -f $(YAML_FILES)

## Carrega as imagens locais no Minikube
load-images:
	@echo "Carregando imagens locais para o Minikube..."
	minikube image load $(PRODUCER_IMAGE)
	minikube image load $(CONSUMER_IMAGE)

## Sobe todos os recursos definidos nos YAMLs
up: load-images
	@echo "Criando namespace (se não existir)..."
	kubectl create namespace $(NAMESPACE) || true
	@echo "Aplicando manifests..."
	kubectl apply -n $(NAMESPACE) -f .

## Remove todos os recursos definidos nos YAMLs
delete:	down

down:
	@echo "Removendo recursos..."
	kubectl delete -n $(NAMESPACE) -f . --ignore-not-found=true

## Mostra status dos pods e services
status:
	@echo "Status dos Pods:"
	kubectl get pods -n $(NAMESPACE)
	@echo "Status dos Services:"
	kubectl get svc -n $(NAMESPACE)

## Mostra logs do Kafka
logs-kafka:
	kubectl logs -n $(NAMESPACE) statefulset/kafka

## Mostra logs do Zookeeper
logs-zookeeper:
	kubectl logs -n $(NAMESPACE) statefulset/zookeeper

## Mostra logs do Producer
logs-producer:
	kubectl logs -n $(NAMESPACE) deployment/producer-service

## Mostra logs do Consumer
logs-consumer:
	kubectl logs -n $(NAMESPACE) deployment/consumer-service

## Limpa tudo (inclusive o namespace)
clean:	down
	@echo "Removendo namespace $(NAMESPACE) e todos os recursos..."
	kubectl delete namespace $(NAMESPACE) --ignore-not-found

port-forward-producer:
	kubectl port-forward -n $(NAMESPACE) svc/producer-service 8080:8080

port-forward-consumer:
	kubectl port-forward -n $(NAMESPACE) svc/consumer-service 8081:8080
