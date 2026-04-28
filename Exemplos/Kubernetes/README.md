# Kubernetes (K8s)

# Problemas conhecidos e suas "soluções"

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