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

## Iniciando...

No diretório `SmartFactory_Lab`:

```bash
$ cd web
$ python -m venv venv
$ source venv/bin/activate
$ pip install -r requirements.txt
$ cd ..
$ minikube status
$ minikube start
$ kubectl apply -f k8s/namespace.yaml
$ kubectl apply -f k8s/mysql.yaml
$ kubectl apply -f k8s/rabbitmq.yaml
$ kubectl get pods -n factoryflow
$ kubectl get service -n factoryflow
$ kubectl run debug --rm -it --image=busybox -n factoryflow -- sh
# nslookup rabbitmq-service
# nslookup mysql-service​
# exit
$ eval $(minikube docker-env)​
$ docker build -t factoryflow-web:v1 ./web
$ docker images
$ kubectl apply -f k8s/web.yaml
$ kubectl get pods -n factoryflow
$ minikube addons enable ingress
$ kubectl get pods -n ingress-nginx
$ kubectl apply -f k8s/ingress.yaml
$ sudo sh -c "echo '`minikube ip` factoryflow.local' >> /etc/hosts"
$ for i in {1..10}; do curl http://factoryflow.local; echo; done
```

**Em caso de problemas**

- Refazer e reiniciar o serviço web:
```bash
$ eval $(minikube docker-env)​
$ docker build -t factoryflow-web:v1 ./web
$ kubectl rollout restart deployment web-deployment -n factoryflow
```

- Depurar problemas usando log:
```bash
kubectl logs -n factoryflow -l app=web
```

**Comandos importantes**

```bash
$ minikube start​
$ minikube status​
$ kubectl get nodes​
$ kubectl version​
$ kubectl create deployment <nome do deploy> --image=<nome da imagem>
$ kubectl get deployment [-n <namespace>]
$ kubectl get pod​
$ kubectl get replicaset​
$ kubectl edit deployment <nome do deploy>
$ kubectl describe pod <nome do pod>
$ kubectl logs <nome do pod>
$ kubectl exec -it <nome do pod> -- /bin/bash​
$ kubectl apply -f <nome do arquivo>.yaml ​
$ kubectl delete -f <nome do arquivo>.yaml ​
$ kubectl delete service <nome do serviço>
$ kubectl delete deployment <nome do serviço>
$ kubectl rollout restart deployment <nome do deploy> -n <namespace>
```
