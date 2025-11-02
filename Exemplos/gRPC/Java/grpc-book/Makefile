# Makefile para construir e gerenciar o projeto gRPC-book em Java
# Variáveis
PROJECT_NAME=grpc-book-inventory
SERVER_DIR=.
CLIENT_DIR=.
DOCKER_SERVER_FILE=Dockerfile.server
DOCKER_CLIENT_FILE=Dockerfile.client

# Nome das imagens
IMAGE_PREFIX=inventory
SERVER_IMAGE=$(IMAGE_PREFIX)-server
CLIENT_IMAGE=$(IMAGE_PREFIX)-client
SERVER_CONTAINER=$(IMAGE_PREFIX)-server
CLIENT_CONTAINER=$(IMAGE_PREFIX)-client

.PHONY: all build docker-build up down run-client logs-server ps \
	status restart-server restart logs clean

# Alvos principais
all: build docker-build

# Compila os módulos Java com Maven
build:
	@echo "==> Compilando o projeto com Maven..."
	mvn clean package

# Gera as imagens Docker
docker-build:
	@echo "==> Construindo imagem do servidor..."
	docker build -t $(SERVER_IMAGE):latest -f $(DOCKER_SERVER_FILE) $(SERVER_DIR)
	@echo "==> Construindo imagem do cliente..."
	docker build -t $(CLIENT_IMAGE):latest -f $(DOCKER_CLIENT_FILE) $(CLIENT_DIR)

# Sobe o ambiente com docker-compose
up:
	@echo "==> Subindo containers..."
	docker compose up -d

# Para e remove os containers
down:
	@echo "==> Parando containers..."
	docker compose down

# Executa o cliente manualmente (se quiser rodar mais de uma vez)
run-client:
	docker run --rm $(CLIENT_IMAGE):latest

# Logs do servidor em tempo real
logs-server:
	@echo "==> Mostrando logs do servidor..."
	docker logs -f $(SERVER_CONTAINER)

# Logs de todos os serviços
logs:
	@echo "==> Mostrando logs de todos os containers do projeto $(PROJECT_NAME)..."
	docker-compose logs -f

# Lista containers ativos do projeto
ps:
	@echo "==> Listando containers do projeto $(PROJECT_NAME)..."
	docker ps -a --filter "name=$(IMAGE_PREFIX)" || true

# Mostra status resumido (Up ou Exited)
status:
	@echo "==> Status dos containers do projeto $(PROJECT_NAME):"
	docker ps -a --filter "name=$(PROJECT_NAME)" --format "table {{.Names}}\t{{.Status}}"

# Reinicia apenas o servidor
restart-server:
	@echo "==> Reiniciando servidor $(SERVER_CONTAINER)..."
	docker restart $(SERVER_CONTAINER)

# Reinicia todos os containers do projeto
restart:
	@echo "==> Reiniciando todos os containers do projeto $(PROJECT_NAME)..."
	docker-compose restart

# Limpeza
clean:
	@echo "==> Limpando builds Maven..."
	mvn clean
	@echo "==> Limpando containers e imagens..."
	docker-compose down -v
	docker rmi -f grpc-book-$(SERVER_IMAGE):latest || true
	docker rmi -f grpc-book-$(CLIENT_IMAGE):latest || true
