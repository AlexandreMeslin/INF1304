# Chat Application with Kafka Cluster

## Comandos interessantes:

```bash
$ docker ps –a
$ docker stop prog_web
$ docker rm prog_web
$ docker rmi fa255d39a2d6
$ docker image prune -a
$ docker images
$ docker exec -it prog_web /bin/bash
$ docker rmi static-site_web
$ docker logs -f consumer_service_1
$ docker inspect chat-app_consumer-service_1
$ docker volume ls
$ docker volume inspect chat-app-cluster_kafka3_logs
```

## Depuração

1. Verificar se os containers estão no ar:
    ```bash
    $ docker ps -a
    ```

    O resultado deverá ser:
    ```text
    CONTAINER ID   IMAGE                                COMMAND                  CREATED          STATUS          PORTS                                                                       NAMES
    7880d80554e6   chat-app-cluster-consumer-service1   "java -jar consumer-…"   15 minutes ago   Up 15 minutes   0.0.0.0:8181->8080/tcp, [::]:8181->8080/tcp                                 chat-app-cluster-consumer-service1-1
    d64bc8b4173f   chat-app-cluster-consumer-service0   "java -jar consumer-…"   15 minutes ago   Up 15 minutes   0.0.0.0:8081->8080/tcp, [::]:8081->8080/tcp                                 chat-app-cluster-consumer-service0-1
    cd444033bdd8   chat-app-cluster-producer-service0   "java -jar producer-…"   15 minutes ago   Up 15 minutes   0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp                                 chat-app-cluster-producer-service0-1
    c655cdfd92fe   chat-app-cluster-producer-service1   "java -jar producer-…"   15 minutes ago   Up 15 minutes   0.0.0.0:8180->8080/tcp, [::]:8180->8080/tcp                                 chat-app-cluster-producer-service1-1
    05e47950a3ff   zookeeper:3.6.3                      "/docker-entrypoint.…"   15 minutes ago   Up 15 minutes   2888/tcp, 3888/tcp, 8080/tcp, 0.0.0.0:2182->2181/tcp, [::]:2182->2181/tcp   chat-app-cluster-zookeeper2-1
    743573734e2f   wurstmeister/kafka:latest            "start-kafka.sh"         15 minutes ago   Up 15 minutes   0.0.0.0:9092->9092/tcp, [::]:9092->9092/tcp                                 kafka1
    4d1420486568   python:3.9-slim                      "python3 -m http.ser…"   15 minutes ago   Up 15 minutes   0.0.0.0:8000->8000/tcp, [::]:8000->8000/tcp                                 chat-app-cluster-frontend-1
    dfd3976bc250   zookeeper:3.6.3                      "/docker-entrypoint.…"   15 minutes ago   Up 15 minutes   2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, [::]:2181->2181/tcp, 8080/tcp   chat-app-cluster-zookeeper1-1
    db5c524ba1f0   wurstmeister/kafka:latest            "start-kafka.sh"         15 minutes ago   Up 15 minutes   0.0.0.0:9093->9093/tcp, [::]:9093->9093/tcp                                 kafka2
    ```

1. Descobrir os hostnames visíveis na rede:

1. Verificar se o tópico existe:
    ```bash
    $ docker exec -it kafka1 kafka-topics.sh --bootstrap-server kafka1:9092 --list
    ```
    Para a versão >=4 (ou faça melhor, personalize a sua imagem incluindo `/opt/kafka/bin na path`):
    ```bash
    $ docker exec -it kafka1 /opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka1:9092 --list
    ```
    ou (atenção para o número da porta!):
    ```bash
    $ docker exec -it kafka2 kafka-topics.sh --bootstrap-server kafka2:9093 --list
    ```

    O resultado deverá ser parecido com:
    ```text
    __consumer_offsets
    chat-messages
    ```

1. Conferir os grupos existentes:
    ```bash
    $ docker exec -it kafka1 kafka-consumer-groups.sh --bootstrap-server kafka1:9092 --list
    ```

    O resultado deve ser parecido com:
    ```text
    chat-consumer-group
    ```

1. Conferir se o consumer entrou no grupo:
    ```bash
    $ docker exec -it kafka1 kafka-consumer-groups.sh --bootstrap-server kafka1:9092 --describe --group chat-consumer-group
    ```

    O resultado deve ser parecido com:
    ```text
    GROUP               TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID                                                         HOST            CLIENT-ID
    chat-consumer-group chat-messages   0          -               1               -               consumer-chat-consumer-group-1-1578dc2c-5c70-4bd7-8de9-cb4fdb49a7c0 /172.20.0.8     consumer-chat-consumer-group-1
    ```

1. Conferir replicação:
    ```bash
    $ docker exec -it kafka1 kafka-topics.sh --bootstrap-server kafka1:9092 --describe --topic chat-messages
    ```

    O resultado deve ser parecido com:
    ```text
    Topic: chat-messages    TopicId: IquOSHpTSn2KOXwydkI4Jg PartitionCount: 1       ReplicationFactor: 1    Configs: segment.bytes=1073741824
            Topic: chat-messages    Partition: 0    Leader: 2       Replicas: 2     Isr: 2
    ```

1. Enviar mensagem manualmente:
    ```bash
    $ docker exec -it kafka1 kafka-console-producer.sh --broker-list kafka1:9092 --topic chat-messages
    ```

    Digite algo como:
    ```text
    >mensagem 3
    >^C
    ```

    Verifique se a mensagem chegou no `chat_listener.py`:
    ```text
    $ python chat_listener.py 
    [INFO] Conectado ao ChatConsumer (Java).
    Aguardando mensagens de broadcast...
    <<< Mensagem recebida: mensagem 3
    ```

1. Conferir manualmente se a mensagem realmente chegou:
    ```text
    $ docker exec -it kafka1 kafka-console-consumer.sh --bootstrap-server kafka1:9092 --topic chat-messages --from-beginning
    ```

    O resultado deve ser parecido com:
    ```text
    Mon Sep 01 16:31:01 UTC 2025 ==> {"user": "user1", "message": "msg1", "timestamp": 1756744261.2142162}
    Mon Sep 01 16:42:55 UTC 2025 ==> {"user": "user1", "message": "msg2", "timestamp": 1756744975.9235358}
    ^CProcessed a total of 2 messages
    ```
