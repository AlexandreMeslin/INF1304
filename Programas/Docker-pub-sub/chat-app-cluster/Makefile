.PHONY: all stop producer consumer build up status frontend clean logs \
	logs-producer logs-consumer logs-kafka logs-zookeeper

# === Variáveis ===
PRODUCER_DIR=producer-service
CONSUMER_DIR=consumer-service
PRODUCER_JAR=$(PRODUCER_DIR)/target/producer-service-jar-with-dependencies.jar
CONSUMER_JAR=$(CONSUMER_DIR)/target/consumer-service-jar-with-dependencies.jar

# Alvo principal (roda tudo - menos o frontend)
all: stop $(PRODUCER_JAR) $(CONSUMER_JAR) build up status

# Para o que estiver rodando
stop:
	@echo "Parando containers..."
	docker ps -a
	docker stop consumer_service0 || true
	docker stop consumer_service1 || true
	docker stop producer_service0 || true
	docker stop producer_service1 || true
	docker stop kafka1 || true
	docker stop kafka2 || true
	docker stop kafka3 || true
	docker stop frontend1 || true
	docker rm consumer_service0 || true
	docker rm consumer_service1 || true
	docker rm producer_service0 || true
	docker rm producer_service1 || true
	docker rm kafka1 || true
	docker rm kafka2 || true
	docker rm kafka3 || true
	docker rm frontend1 || true
	docker ps -a

# Compila o produtor
$(PRODUCER_JAR): $(wildcard $(PRODUCER_DIR)/src/main/java/**/*.java) $(PRODUCER_DIR)/pom.xml
	@echo "Compilando Producer..."
	cd $(PRODUCER_DIR) && mvn clean package

# Compila o consumidor
$(CONSUMER_JAR): $(wildcard $(CONSUMER_DIR)/src/main/java/**/*.java) $(CONSUMER_DIR)/pom.xml
	@echo "Compilando Consumer..."
	cd $(CONSUMER_DIR) && mvn clean package

# Constrói as imagens
build: $(PRODUCER_JAR) $(CONSUMER_JAR)
	@echo "Criando os containers..."
	docker-compose build

# Sobe os containers
up:
	@echo "Subindo os containers..."
	docker-compose up -d

# Lista os containers
status:
	@echo "Status dos containers:"
	docker ps -a

# Mostra logs em tempo real
logs:
	@echo "Mostrando logs dos containers (Ctrl+C para sair)..."
	docker-compose logs -f

# Logs separados
logs-producer:
	@echo "Mostrando logs do Producer..."
	docker-compose logs -f producer-service

logs-consumer:
	@echo "Mostrando logs do Consumer..."
	docker-compose logs -f consumer-service

logs-kafka:
	@echo "Mostrando logs do Kafka..."
	docker-compose logs -f kafka

logs-zookeeper:
	@echo "Mostrando logs do Zookeeper..."
	docker-compose logs -f zookeeper

# Sobe o frontend
# deprecated - frontend movido para um container
frontend:
	@echo "Iniciando frontend em http://localhost:8000"
	cd frontend && python3 -m http.server --bind 0.0.0.0 8000

# Limpa build do produtor e consumidor
clean:	stop
	@echo "Limpando builds..."
	cd $(PRODUCER_DIR) && mvn clean
	cd $(CONSUMER_DIR) && mvn clean
	docker ps -a
