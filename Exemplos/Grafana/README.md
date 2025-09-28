# Grafana + Prometheus

## Exemplo de monitoração de containers docker

1. Subir os containers:
    ```
    docker compose up -d
    ```
    Resultado esperado:
    ```
    WARN[0000] /workspaces/INF1304/Exemplos/Grafana/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion 
    [+] Running 22/22
    ✔ grafana Pulled                                               29.0s 
    ✔ prometheus Pulled                                            19.6s 
    [+] Running 3/3
    ✔ Network grafana_default  Created                              0.1s 
    ✔ Container prometheus     Started                              1.0s 
    ✔ Container grafana        Started                              1.3s 
    ```
    - Prometheus estará disponível em: http://localhost:9090
    - Grafana estará disponível em: http://localhost:3000
(login padrão: admin / admin)
    
    Expor a porta no ambiente:
    - No painel do Codespace → Ports → adicionar a porta 3000 como Public.
    - O mesmo para acessar o Prometheus na porta 9090.

1. Configurar Grafana

    1. Acesse o Grafana no navegador (pela URL exposta).
    1. Vá em Connections → Data sources → Add data source.
    1. Escolha Prometheus.
    1. Configure a URL como:
        ```url
        http://prometheus:9090
        ```
        > (Grafana e Prometheus estão na mesma rede do Docker Compose).
    1. Clique em Save & Test.

1. Criar um Dashboard Simples
    - Vá em Dashboards → New → Add visualization.
    - Selecione a fonte de dados Prometheus.
    - Use a query:
    ```
    rate(prometheus_http_requests_total[1m])
    ```
    Isso mostra a taxa de requisições HTTP que o Prometheus está processando.
    - Salve o painel → pronto 🎉

1. Como usar em sala de aula
    Você pode propor o seguinte exercício:
    1. Subir Prometheus + Grafana no Codespace com docker-compose.
    1. Configurar o Prometheus como data source no Grafana.
    1. Criar um painel simples que mostre:
    - Número de requisições HTTP no Prometheus.
    - Latência média de requisições (prometheus_http_request_duration_seconds_bucket).

    Extensão do exercício:
    - Adicionar um container extra (ex.: um app em Python com /metrics usando Prometheus client).
    - Fazer os alunos configurarem o Prometheus para coletar métricas desse app e exibi-las no Grafana.
