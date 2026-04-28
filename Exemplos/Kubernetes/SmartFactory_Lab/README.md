# SmartFactory Lab

Um experimento para demonstrar um sistema distribuído e concorrente.

## Objetivo

Demonstrar de forma prática:
- Pods e containers
- Deployments
- Services
- ConfigMaps
- Secrets
- Volumes Persistentes
- Ingress
- Escalabilidade horizontal
- Balanceamento de carga
- Falha e auto-recuperação
- Logs
- Monitoramento
- Comunicação entre containers
- Arquitetura distribuída e concorrente
- Mensageria

## Cenário

Uma empresa fictícia que possui:

- Website institucional
- Login centralizado
- Banco de dados
- Servidor de arquivos
- Dashboard de métricas
- Fila de mensagens
- Cache
- Backup automático
- Sistema de log
- Sensores IoT (ou IoMT)

## Tecnologias utilizadas

Aplicações:
- Node.js (Java, no futuro): frontend
- MySQL: banco de dados
- Keycloak: autenticação
- MinIO: servidor de arquivos
- Prometheus: monitoramento - métricas
- Grafana: monitoramento - dashboard
- Redis: cache (ainda em dúvida se será incluído)
- RabbitMQ: mensageria
- CronJob: backup automático

Infraestrutura:
- Kubernetes
- Docker
- Spring Boot (no futuro, junto com Java)

Desenvolvimento
- GitHub
- Codespace
  - GitHub Copilot Chat
- Pacotes Linux
  - iputils-ping
  