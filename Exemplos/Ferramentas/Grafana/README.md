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

    1. Acessar o Grafana no navegador (pela URL exposta).

    1. Ir em `Connections` → `Data sources` → `Add data source`.

        ![`Connections` → `Data sources` → `Add data source`](img/Grafana-Connections-DataSource.png)


        ![`Connections` → `Data sources` → `Add data source`](img/Grafana-Connections-DataSource.png)
    1. Escolher Prometheus.

        ![Escolher Prometheus](img/Grafana-DataSource-Prometheus.png)
    1. Configurar a URL como:
        > (mesmo usando o Codespace e redirecionando, o acesso é realizado dentro do Codespace, logo o endereço é esse mesmo mostrado a seguir)
    1. Escolher Prometheus.

        ![Escolher Prometheus](img/Grafana-DataSource-Prometheus.png)

    1. Configurar a URL como:

        > (mesmo usando o Codespace e redirecionando, o acesso é realizado dentro do Codespace, logo o endereço é esse mesmo mostrado a seguir)

    1. Escolher Prometheus.

    1. Configurar a URL como:

        ```url
        http://prometheus:9090
        ```

        > (Grafana e Prometheus estão na mesma rede do Docker Compose).

    1. Clicar em `Save & Test`.

        ![`Save & Test`](img/Grafana-SaveTest.png)

        ![`Save & Test`](img/Grafana-SaveTest.png)

1. Criar um Dashboard Simples

    - Ir em `Dashboards` → `New` → `Add visualization`.
    > Se não houver nenhum `Dashboard` criado anteriomente, clicar no botão `+ Create dashboard`.    
    > Clicar no botão `+ Add visualization`.

1. Criar um Dashboard Simples
    - Ir em `Dashboards` → `New` → `Add visualization`.
    - Selecionar a fonte de dados Prometheus.
    - Usar a query:

    ```
    rate(prometheus_http_requests_total[1m])
    ```

    Isso mostra a taxa de requisições HTTP que o Prometheus está processando.
    - Salvar o painel.
    
        ![Salvar o painel](img/Grafana-discard-save-dashboard.png)

1. Uso

    1. Subir Prometheus + Grafana no Codespace com docker-compose.
    1. Configurar o Prometheus como data source no Grafana.
    1. Criar um painel simples que mostre:
    - Número de requisições HTTP no Prometheus.
    - Latência média de requisições (prometheus_http_request_duration_seconds_bucket).

    Extensão do exercício:
    - Adicionar um container extra (ex.: um app em Python com /metrics usando Prometheus client).
    - Configurar o Prometheus para coletar métricas desse app e exibir no Grafana.
