# Usando K8S

## Comandos

1. Criar o namespace:

    ```bash
    $ minikube start
    $ kubectl config use-context minikube
    $ minikube status
    $ kubectl cluster-info
    $ kubectl apply -f namespace.yaml
    ```

1. Aplicar os manifests

    ```bash
    $ kubectl apply -f zookeeper.yaml
    $ kubectl apply -f kafka.yaml
    $ kubectl apply -f producer.yaml
    $ kubectl apply -f consumer.yaml
    ```

1. Verificar os PODs

    ```bash
    $ kubectl get pods -n kafka-demo
    $ kubectl get svc -n kafka-demo
    ```

1. Parar a aplicação

    Toda a aplicação de uma vez:

    ```bash
    $ kubectl delete namespace kafka-demo
    ```

    Parando cada um dos microsserviçoes:

    ```bash
    $ kubectl delete -f producer.yaml -n kafka-demo
    $ kubectl delete -f consumer.yaml -n kafka-demo
    $ kubectl delete -f kafka.yaml -n kafka-demo
    $ kubectl delete -f zookeeper.yaml -n kafka-demo
    $ kubectl delete -f namespace.yaml
    ```




TODO: Montar uma versão com 3 réplicas de Kafka e 3 de Zookeeper, mostrando como configurar a replicação de tópicos para tolerância a falhas
