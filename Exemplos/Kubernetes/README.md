# Kubernetes (K8s)

# Comandos mais comuns

```bash
$ sudo k3s kubectl get nodes
NAME               STATUS   ROLES           AGE   VERSION
ip-172-31-69-127   Ready    <none>          14h   v1.36.4+k3s1
ip-172-31-73-168   Ready    control-plane   15h   v1.36.4+k3s1
ip-172-31-76-143   Ready    <none>          15h   v1.36.4+k3s1
```
```bash
$ sudo kubectl apply -f k8s/namespace.yaml
namespace/factoryflow created
```
```bash
$ sudo kubectl delete -f k8s/mysql.yaml
namespace "factoryflow" deleted
```
```bash
$ sudo kubectl delete pod debug -n factoryflow
pod "debug" deleted from factoryflow namespace
```
```bash
$ sudo kubectl get endpoints -n factoryflow
NAME               ENDPOINTS                        AGE
mysql-service      10.42.2.6:3306                   6m56s
rabbitmq-service   10.42.1.8:5672,10.42.1.8:15672   6m41s
```
```bash
$ sudo kubectl get nodes -o wide
NAME               STATUS   ROLES           AGE   VERSION        INTERNAL-IP     EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION            CONTAINER-RUNTIME
ip-172-31-69-127   Ready    <none>          14h   v1.36.4+k3s1   172.31.69.127   <none>        Ubuntu 24.04.4 LTS   6.17.0-1017-aws (amd64)   containerd://2.3.4-k3s1.36
ip-172-31-73-168   Ready    control-plane   15h   v1.36.4+k3s1   172.31.73.168   <none>        Ubuntu 24.04.4 LTS   6.17.0-1017-aws (amd64)   containerd://2.3.4-k3s1.36
ip-172-31-76-143   Ready    <none>          15h   v1.36.4+k3s1   172.31.76.143   <none>        Ubuntu 24.04.4 LTS   6.17.0-1017-aws (amd64)   containerd://2.3.4-k3s1.36
```
```bash
$ sudo kubectl get pods -n factoryflow -o wide
NAME                                   READY   STATUS    RESTARTS   AGE     IP          NODE               NOMINATED NODE   READINESS GATES
mysql-deployment-5c4f7949cb-fp2fh      1/1     Running   0          8m4s    10.42.2.6   ip-172-31-69-127   <none>           <none>
rabbitmq-deployment-64d55d7f97-5mttb   1/1     Running   0          7m50s   10.42.1.8   ip-172-31-76-143   <none>           <none>
```
```bash
$ sudo kubectl get services -n factoryflow
NAME               TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)              AGE
mysql-service      ClusterIP   10.43.155.197   <none>        3306/TCP             8m32s
rabbitmq-service   ClusterIP   10.43.110.25    <none>        5672/TCP,15672/TCP   8m18s
```
```bash
$ sudo kubectl label node ip-172-31-7-142 mysql=true
node/ip-172-31-73-168 labeled
```
```bash
$ sudo kubectl port-forward --address 0.0.0.0 -n factoryflow svc/rabbitmq-service 15672:15672
Forwarding from 0.0.0.0:15672 -> 15672
```
```bash
$ sudo kubectl rollout restart deployment web-deployment -n factoryflow
deployment.apps/web-deployment restarted
```
```bash
$ sudo kubectl run debug --rm -it --image=busybox -n factoryflow -- sh
All commands and output from this session will be recorded in container logs, including credentials and sensitive information passed through the command prompt.
If you don't see a command prompt, try pressing enter.
/ # 
```
```bash
$ sudo kubectl scale deployment web-deployment --replicas=3 -n factoryflow
deployment.apps/web-deployment scaled
```

# Problemas conhecidos e suas "soluções"

## Problemas ao usar DNS em um container

### Descrição

Ao tentar resolver algum nome, o cliente DNS acaba dando timeout.

### Sintoma

<não capturei o sintoma>

### Solução

#### Primeira tentativa

Verifique as portas no **Security group**, principalmente a porta UDP 8472 do **VXLAN**.

#### Segunda tentativa

Verifique se o mesmo **Security group** foi aplicado a todas as EC2.
Caso seja necessário modificar o **Security group** de alguma EC2, vá até a console da instância e clique em **Achtion** > **Security** > **Change security groups**.

## Erro ao criar um deployment

### Descrição

Erro ao tentar criar ou aplicar um *deployment*.

Um dos motivos mais prováveis é a falta do `minikube`.

### Sintoma

```bash
$ kubectl apply -f nginx-deployment.yaml 
error: error validating "nginx-deployment.yaml": error validating data: failed to download openapi: Get "https://192.168.49.2:8443/openapi/v2?timeout=32s": dial tcp 192.168.49.2:8443: connect: no route to host; if you choose to ignore these errors, turn validation off with --validate=false
```

### Solução

Execute o `minikube` e tente novamente:

```bash
$ minikube start
😄  minikube v1.38.1 on Ubuntu 24.04 (docker/amd64)
✨  Using the docker driver based on existing profile
👍  Starting "minikube" primary control-plane node in "minikube" cluster
🚜  Pulling base image v0.0.50 ...
🔄  Restarting existing docker container for "minikube" ...
🐳  Preparing Kubernetes v1.35.1 on Docker 29.2.1 ...
🔎  Verifying Kubernetes components...
    ▪ Using image gcr.io/k8s-minikube/storage-provisioner:v5
🌟  Enabled addons: storage-provisioner, default-storageclass
🏄  Done! kubectl is now configured to use "minikube" cluster and "default" namespace by default
```