# Microserviço de Inventário de Livros (gRPC)

Este exercício envolve a criação de dois componentes em containers Docker: um Servidor que hospeda a lógica de negócios e um Cliente que faz as chamadas remotas de procedimento (RPC).

## Objetivo

Implementar um microserviço de Inventário de Livros (BookInventoryService) usando gRPC para demonstrar:

1. A definição de um contrato de serviço (.proto).
1. Comunicação remota entre dois containers diferentes.
1. Uso de diferentes tipos de RPC (Unary e Streaming).

## Definição do Protocolo (Contrato)

Mensagens (Estruturas de Dados):
- Book: (ISBN, Title, Author, Year, StockCount).
- BookRequest: (ISBN) para buscar um livro específico.
- SearchRequest: (Author) para buscar todos os livros de um autor.

## Serviço (BookInventoryService): O serviço deve expor os seguintes métodos RPC:

| Método RPC | Tipo de RPC | Descrição |
|:---:|:---:|-----------|
| `GetBook` | Unary       | O cliente envia um BookRequest (ISBN) e o servidor responde com um único Book.|
| `AddBook` | Unary       | O cliente envia um novo Book e o servidor responde com uma mensagem de sucesso/status.|
| `SearchBooks` | Server-Streaming | O cliente envia um SearchRequest (Author) e o servidor envia de volta um stream de mensagens Book que correspondem ao autor.|

## Implementação do Servidor

O servidor será o "microserviço de inventário".
- Lógica: O servidor deve manter uma lista (pode ser um dicionário simples em memória) de objetos Book.
- Ações: Implementar a lógica para os três métodos RPC definidos acima.
- Infraestrutura: O servidor deve ser empacotado em um container Docker e exposto em uma porta gRPC (ex: 50051).

## Implementação do Cliente

O cliente será o aplicativo que interage com o inventário.
- Conexão: O cliente deve estabelecer uma conexão gRPC com o servidor usando o nome do serviço (por exemplo, inventory-server:50051) fornecido pela rede Docker.
- Ações: O cliente deve:
    1. Chamar AddBook para adicionar pelo menos 5 livros (com autores diferentes).
    1. Chamar GetBook com o ISBN de um dos livros para confirmar a adição.
    1. Chamar SearchBooks usando um nome de autor e imprimir o stream de livros retornados.
- Infraestrutura: O cliente também deve ser empacotado em um container Docker.

## Estrutura de Diretórios

```
grpc-book-inventory/
├── docker-compose.yml
├── inventory.proto
├── server/
│   ├── Dockerfile
│   └── src/
│       └── main/
│           ├── java/
│           │   └── br/com/meslin/inventory/server/
│           │       └── InventoryServer.java
│           └── resources/
│               └── log4j.properties
│   └── pom.xml
└── client/
    ├── Dockerfile
    └── src/
        └── main/
            ├── java/
            │   └── br/com/meslin/inventory/client/
            │       └── InventoryClient.java
            └── resources/
                └── log4j.properties
    └── pom.xml
```