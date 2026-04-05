# K8s Resiliente

## Estrutura de arquivos
```
k8s/
 ├── README.md
 ├── Makefile
 ├── 00-namespace.yml
 ├── zookeeper-statefulset.yml
 ├── kafka-statefulset.yml
 ├── producer-deployment.yml
 ├── consumer-deployment.yml
```

## Comandos do Makefile
```bash
$ make up
$ make status
$ make logs-kafka
$ make logs-producer
$ make logs-consumer
$ make down
$ make clean
```

## Executando...

1. Verificar o estado do Minikube:

    Comando:
    ```bash
    chat-app-k8s-resilient (main) $ minikube status
    ```
    O Minikube precisa ser iniciado se o resultado for uma das duas saídas:

    **Saída 1:**
    ```bash
    🤷  Profile "minikube" not found. Run "minikube profile list" to view all profiles.
    👉  To start a cluster, run: "minikube start"
    ```
    **Saída 2:**
    ```bash
    minikube
    type: Control Plane
    host: Stopped
    kubelet: Stopped
    apiserver: Stopped
    kubeconfig: Stopped
    ```
    Por outro lado, se o resultado for compatível com o seguinte, o Minikube está pronto:
    ```bash
    minikube
    type: Control Plane
    host: Running
    kubelet: Running
    apiserver: Running
    kubeconfig: Configured
    ```

    Se o Minikube estiver parado, iniciar o Minikube (vai demorar):
    ```bash
    chat-app-k8s-resilient (main) $ minikube start
    ```
    Saída esperada:
    ```bash
    😄  minikube v1.36.0 on Ubuntu 24.04 (docker/amd64)
    ✨  Using the docker driver based on existing profile
    👍  Starting "minikube" primary control-plane node in "minikube" cluster
    🚜  Pulling base image v0.0.47 ...
    🔄  Restarting existing docker container for "minikube" ...
    🐳  Preparing Kubernetes v1.33.1 on Docker 28.1.1 ...
    🔎  Verifying Kubernetes components...
        ▪ Using image gcr.io/k8s-minikube/storage-provisioner:v5
    🌟  Enabled addons: default-storageclass, storage-provisioner
    🏄  Done! kubectl is now configured to use "minikube" cluster and "default" namespace by default
    ```

1. Criar as imagens

    No diretório chat-app:
    ```bash
    chat-app (main) $ ./build-image.sh 
    ```
    Saída esperada:
    ```text
    [INFO] Scanning for projects...
    [INFO] 
    [INFO] -------------------< br.com.meslin:producer-service >-------------------
    [INFO] Building producer-service 1.0-SNAPSHOT
    [INFO]   from pom.xml
    [INFO] --------------------------------[ jar ]---------------------------------
    [INFO] 
    [INFO] --- clean:3.2.0:clean (default-clean) @ producer-service ---
    [INFO] Deleting /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/producer-service/target
    [INFO] 
    [INFO] --- resources:3.3.1:resources (default-resources) @ producer-service ---
    [INFO] skip non existing resourceDirectory /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/producer-service/src/main/resources
    [INFO] 
    [INFO] --- compiler:3.8.1:compile (default-compile) @ producer-service ---
    [INFO] Changes detected - recompiling the module!
    [INFO] Compiling 2 source files to /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/producer-service/target/classes
    [INFO] 
    [INFO] --- resources:3.3.1:testResources (default-testResources) @ producer-service ---
    [INFO] skip non existing resourceDirectory /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/producer-service/src/test/resources
    [INFO] 
    [INFO] --- compiler:3.8.1:testCompile (default-testCompile) @ producer-service ---
    [INFO] No sources to compile
    [INFO] 
    [INFO] --- surefire:3.2.5:test (default-test) @ producer-service ---
    [INFO] No tests to run.
    [INFO] 
    [INFO] --- assembly:3.3.0:single (make-assembly) @ producer-service ---
    [INFO] Building jar: /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/producer-service/target/producer-service-jar-with-dependencies.jar
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  8.199 s
    [INFO] Finished at: 2025-08-25T21:36:39Z
    [INFO] ------------------------------------------------------------------------

    [+] Building 2.1s (9/9) FINISHED                                                                                                                                                           docker:default
    => [internal] load build definition from Dockerfile                                                                                                                                                 0.0s
    => => transferring dockerfile: 215B                                                                                                                                                                 0.0s
    => [internal] load metadata for docker.io/library/openjdk:11-jre-slim                                                                                                                               0.1s
    => [auth] library/openjdk:pull token for registry-1.docker.io                                                                                                                                       0.0s
    => [internal] load .dockerignore                                                                                                                                                                    0.0s
    => => transferring context: 2B                                                                                                                                                                      0.0s
    => [1/3] FROM docker.io/library/openjdk:11-jre-slim@sha256:93af7df2308c5141a751c4830e6b6c5717db102b3b31f012ea29d842dc4f2b02                                                                         0.0s
    => [internal] load build context                                                                                                                                                                    0.2s
    => => transferring context: 20.33MB                                                                                                                                                                 0.2s
    => CACHED [2/3] WORKDIR /app                                                                                                                                                                        0.0s
    => [3/3] COPY target/producer-service-jar-with-dependencies.jar producer-service.jar                                                                                                                0.8s
    => exporting to image                                                                                                                                                                               0.9s
    => => exporting layers                                                                                                                                                                              0.9s
    => => writing image sha256:6d8fd114ef43daa0cf9cdd8e17f527e8501b6a0a3ac4a3fda18ae980e3027cc7                                                                                                         0.0s
    => => naming to docker.io/library/producer-service:latest                                                                                                                                           0.0s
    [INFO] Scanning for projects...
    [INFO] 
    [INFO] -------------------< br.com.meslin:consumer-service >-------------------
    [INFO] Building consumer-service 1.0-SNAPSHOT
    [INFO]   from pom.xml
    [INFO] --------------------------------[ jar ]---------------------------------
    [INFO] 
    [INFO] --- clean:3.2.0:clean (default-clean) @ consumer-service ---
    [INFO] Deleting /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/consumer-service/target
    [INFO] 
    [INFO] --- resources:3.3.1:resources (default-resources) @ consumer-service ---
    [INFO] skip non existing resourceDirectory /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/consumer-service/src/main/resources
    [INFO] 
    [INFO] --- compiler:3.8.1:compile (default-compile) @ consumer-service ---
    [INFO] Changes detected - recompiling the module!
    [INFO] Compiling 2 source files to /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/consumer-service/target/classes
    [INFO] 
    [INFO] --- resources:3.3.1:testResources (default-testResources) @ consumer-service ---
    [INFO] skip non existing resourceDirectory /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/consumer-service/src/test/resources
    [INFO] 
    [INFO] --- compiler:3.8.1:testCompile (default-testCompile) @ consumer-service ---
    [INFO] No sources to compile
    [INFO] 
    [INFO] --- surefire:3.2.5:test (default-test) @ consumer-service ---
    [INFO] No tests to run.
    [INFO] 
    [INFO] --- assembly:3.3.0:single (make-assembly) @ consumer-service ---
    [INFO] Building jar: /workspaces/INF1304/Exemplos/Docker-pub-sub/chat-app/consumer-service/target/consumer-service-jar-with-dependencies.jar
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  7.639 s
    [INFO] Finished at: 2025-08-25T21:36:51Z
    [INFO] ------------------------------------------------------------------------

    [+] Building 1.4s (8/8) FINISHED                                                                                                                                                           docker:default
    => [internal] load build definition from Dockerfile                                                                                                                                                 0.0s
    => => transferring dockerfile: 215B                                                                                                                                                                 0.0s
    => [internal] load metadata for docker.io/library/openjdk:11-jre-slim                                                                                                                               0.1s
    => [internal] load .dockerignore                                                                                                                                                                    0.0s
    => => transferring context: 2B                                                                                                                                                                      0.0s
    => [1/3] FROM docker.io/library/openjdk:11-jre-slim@sha256:93af7df2308c5141a751c4830e6b6c5717db102b3b31f012ea29d842dc4f2b02                                                                         0.0s
    => [internal] load build context                                                                                                                                                                    0.1s
    => => transferring context: 20.05MB                                                                                                                                                                 0.1s
    => CACHED [2/3] WORKDIR /app                                                                                                                                                                        0.0s
    => [3/3] COPY target/consumer-service-jar-with-dependencies.jar consumer-service.jar                                                                                                                0.8s
    => exporting to image                                                                                                                                                                               0.3s
    => => exporting layers                                                                                                                                                                              0.3s
    => => writing image sha256:781b64d90f784006dc9a0d49f409d2346426c26621c2d8390b6afee7f2b3b442                                                                                                         0.0s
    => => naming to docker.io/library/consumer-service:latest
    ```

1. Subir as imagens no Minikube
    ```bash
    chat-app-k8s-resilient (main) $ make up
    ```
    Saída esperada:
    ```bash
    Carregando imagens locais para o Minikube...
    minikube image load producer-service:latest
    minikube image load consumer-service:latest
    Criando namespace (se não existir)...
    kubectl create namespace kafka-demo || true
    namespace/kafka-demo created
    Aplicando manifests...
    kubectl apply -n kafka-demo -f .
    Warning: resource namespaces/kafka-demo is missing the kubectl.kubernetes.io/last-applied-configuration annotation which is required by kubectl apply. kubectl apply should only be used on resources created declaratively by either kubectl create --save-config or kubectl apply. The missing annotation will be patched automatically.
    namespace/kafka-demo configured
    service/consumer-service created
    deployment.apps/consumer-service created
    service/kafka created
    statefulset.apps/kafka created
    service/producer-service created
    deployment.apps/producer-service created
    service/zookeeper created
    statefulset.apps/zookeeper created
    ```

    O comando `make up` é equivalente a usar os seguintes comandos:
    ```bash
    $ minikube image load producer-service:latest
    $ minikube image load consumer-service:latest
    $ kubectl create namespace kafka-demo
    $ kubectl apply -n kafka-demo -f .
    ```

1. Para verificar o estado:
    ```bash
    chat-app-k8s-resilient (main) $ make status
    ```
    Saída:
    ```bash
    Status dos Pods:
    kubectl get pods -n kafka-demo
    NAME                               READY   STATUS    RESTARTS   AGE
    consumer-service-bf64f8d44-cfmvl   1/1     Running   0          8m50s
    kafka-0                            1/1     Running   0          8m50s
    producer-service-646cb6c5d-g2lkm   1/1     Running   0          8m50s
    zookeeper-0                        1/1     Running   0          8m49s
    Status dos Services:
    kubectl get svc -n kafka-demo
    NAME               TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
    consumer-service   NodePort    10.102.45.53    <none>        8080:30081/TCP   8m51s
    kafka              ClusterIP   None            <none>        9092/TCP         8m50s
    producer-service   NodePort    10.103.22.242   <none>        8080:30080/TCP   8m50s
    zookeeper          ClusterIP   None            <none>        2181/TCP         8m50s    
    ```

1. Testar os serviços:
    ```bash
    chat-app-k8s-resilient (main) $ minikube ip
    ```
    Saída:
    ```bash
    192.168.49.2
    ```

1. Redirecione as portas:
    Verifique se as portas do produtor e do consumidor já foram redirecionadas.
    ```bash
    chat-app-k8s-resilient (main) $ lsof -i -P -n | grep kubectl | grep -i listen
    ```
    Resultado:
    ```text
    kubectl 39118 codespace    8u  IPv4 354059      0t0  TCP 127.0.0.1:8080 (LISTEN)
    kubectl 39118 codespace    9u  IPv6 354061      0t0  TCP [::1]:8080 (LISTEN)
    kubectl 41513 codespace    8u  IPv4 383845      0t0  TCP 127.0.0.1:8081 (LISTEN)
    kubectl 41513 codespace    9u  IPv6 384634      0t0  TCP [::1]:8081 (LISTEN)
    ```    
    Se não tiverem sido, redirecione-as.
    * Porta do produtor:
    ```bash
    chat-app/frontend (main) $ kubectl port-forward svc/producer-service -n kafka-demo 8080:8080 > /dev/null &
    ```
    Resultado
    ```bash
    Forwarding from 127.0.0.1:8080 -> 8080
    Forwarding from [::1]:8080 -> 8080
    ```
    * Porta do consumidor
    ```bash
    chat-app/frontend (main) $ kubectl port-forward svc/consumer-service -n kafka-demo 8081:8080 > /dev/null &
    ```
    Resultado:
    ```bash
    Forwarding from 127.0.0.1:8081 -> 8080
    Forwarding from [::1]:8081 -> 8080
    ```

1. Para parar toda a aplicação (pode demorar um pouco):
    ```bash
    chat-app-k8s-resilient (main) $ make down
    ```
    Resultado:
    ```bash
    Removendo recursos...
    kubectl delete -n kafka-demo -f .
    Warning: deleting cluster-scoped resources, not scoped to the provided namespace
    namespace "kafka-demo" deleted
    service "consumer-service" deleted
    deployment.apps "consumer-service" deleted
    service "kafka" deleted
    statefulset.apps "kafka" deleted
    service "producer-service" deleted
    deployment.apps "producer-service" deleted
    service "zookeeper" deleted
    statefulset.apps "zookeeper" deleted
    ```
    Comando:
    ```bash
    chat-app-k8s-resilient (main) $ make clean
    ```
    Resultado:
    ```bash
    Removendo namespace kafka-demo e todos os recursos...
    kubectl delete namespace kafka-demo --ignore-not-found
    ```

## Problemas conhecidos e suas "soluções"

1. Erro ao iniciar o `minikube`:
    - Sintoma:
        ```bash
        $ minikube start
        😄  minikube v1.36.0 on Ubuntu 24.04 (docker/amd64)
        ✨  Using the docker driver based on existing profile
        👍  Starting "minikube" primary control-plane node in "minikube" cluster
        🚜  Pulling base image v0.0.47 ...
        🤷  docker "minikube" container is missing, will recreate.
        🔥  Creating docker container (CPUs=2, Memory=2200MB) ...
        🐳  Preparing Kubernetes v1.33.1 on Docker 28.1.1 ...
        🔎  Verifying Kubernetes components...
            ▪ Using image gcr.io/k8s-minikube/storage-provisioner:v5
        ❗  Enabling 'storage-provisioner' returned an error: running callbacks: [sudo KUBECONFIG=/var/lib/minikube/kubeconfig /var/lib/minikube/binaries/v1.33.1/kubectl apply --force -f /etc/kubernetes/addons/storage-provisioner.yaml: Process exited with status 1
        stdout:

        stderr:
        error: error validating "/etc/kubernetes/addons/storage-provisioner.yaml": error validating data: failed to download openapi: Get "https://localhost:8443/openapi/v2?timeout=32s": dial tcp [::1]:8443: connect: connection refused; if you choose to ignore these errors, turn validation off with --validate=false
        ]
        ❗  Enabling 'default-storageclass' returned an error: running callbacks: [sudo KUBECONFIG=/var/lib/minikube/kubeconfig /var/lib/minikube/binaries/v1.33.1/kubectl apply --force -f /etc/kubernetes/addons/storageclass.yaml: Process exited with status 1
        stdout:

        stderr:
        error: error validating "/etc/kubernetes/addons/storageclass.yaml": error validating data: failed to download openapi: Get "https://localhost:8443/openapi/v2?timeout=32s": dial tcp [::1]:8443: connect: connection refused; if you choose to ignore these errors, turn validation off with --validate=false
        ]
        🌟  Enabled addons: 

        ❌  Exiting due to K8S_APISERVER_MISSING: wait 6m0s for node: wait for apiserver proc: apiserver process never appeared
        💡  Suggestion: Check that the provided apiserver flags are valid, and that SELinux is disabled
        🍿  Related issues:
            ▪ https://github.com/kubernetes/minikube/issues/4536
            ▪ https://github.com/kubernetes/minikube/issues/6014    
        ```
    - Causa (*root-cause*):

        Provavelmente o `minikube` tentou iniciar com uma configuração errada ou corrompida

    - Solução:

        Apagar as configurações:
        ```bash
        $ minikube delete --all --purge
        ```
        Resultado esperado:
        ```bash
        🔥  Deleting "minikube" in docker ...
        🔥  Removing /home/codespace/.minikube/machines/minikube ...
        💀  Removed all traces of the "minikube" cluster.
        🔥  Successfully deleted all profiles
        💀  Successfully purged minikube directory located at - [/home/codespace/.minikube]
        📌  Kicbase images have not been deleted. To delete images run:
            ▪ docker rmi gcr.io/k8s-minikube/kicbase:v0.0.47
        ```


