# Aplicação Web SmartFactory

## Enviar versão para o Docker Hub

- Fazer *login*:

```bash
$ sudo docker login -u meslin
WARNING! Your password will be stored unencrypted in /root/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credential-stores

Login Succeeded
```

- Criar a imagem:

```bash
$ sudo docker build -t factoryflow-web:v1 .
[+] Building 40.5s (11/11) FINISHED                                                                                                                   docker:default
 => [internal] load build definition from Dockerfile                                                                                                            0.0s
 => => transferring dockerfile: 352B                                                                                                                            0.0s
 => [internal] load metadata for docker.io/library/python:3.12-slim                                                                                             0.6s
 => [internal] load .dockerignore                                                                                                                               0.0s
 => => transferring context: 2B                                                                                                                                 0.0s
 => [internal] load build context                                                                                                                              26.9s
 => => transferring context: 50.72MB                                                                                                                           26.8s
 => [1/6] FROM docker.io/library/python:3.12-slim@sha256:c3d81d25b3154142b0b42eb1e61300024426268edeb5b5a26dd7ddf64d9daf28                                       0.0s
 => CACHED [2/6] WORKDIR /app                                                                                                                                   0.0s
 => CACHED [3/6] RUN apt-get update && apt-get install -y     gcc     default-libmysqlclient-dev     pkg-config                                                 0.0s
 => [4/6] COPY requirements.txt .                                                                                                                               0.3s
 => [5/6] RUN pip install --no-cache-dir -r requirements.txt                                                                                                   10.1s
 => [6/6] COPY . .                                                                                                                                              1.1s 
 => exporting to image                                                                                                                                          1.2s 
 => => exporting layers                                                                                                                                         1.1s 
 => => writing image sha256:ef9722c26409d9909524bf39372badc922473e74893576b6dbea1c78d9c5d714                                                                    0.0s 
 => => naming to docker.io/library/factoryflow-web:v1 
 ```

 - Verificar a imagem:

 ```bash
 $ sudo docker images
REPOSITORY        TAG               IMAGE ID       CREATED         SIZE
factoryflow-web   v1                ef9722c26409   3 minutes ago   450MB
 ```
- Fazer o upload para o *Docker Hub*:

```bash
$ sudo docker tag factoryflow-web:v1 meslin/factoryflow-web:v1

$ sudo docker push meslin/factoryflow-web:v1
The push refers to repository [docker.io/meslin/factoryflow-web]
9771093362b4: Pushed 
d4c1893f4797: Pushed 
a92b7013d56c: Pushed 
7b7085a7bd67: Pushed 
da921d475855: Pushed 
b80f3ed1ee6d: Mounted from library/python 
83fdf57f71f2: Mounted from library/python 
ccbaccfc0388: Mounted from library/python 
f2ec4de84f55: Mounted from library/python 
v1: digest: sha256:39e2c1b2732b71e922c89e4c67addb2632f09c7dfe6f57e94481f719aa124bd0 size: 2208
```
