/**
 * @file rpc_server.c
 * @brief Servidor RPC simples em C usando JSON sobre TCP
 * 
 * Compilar com: gcc -Wall -o rpc_server rpc_server.c -std=c11
 */

/*
 * Cabeçalhos (não são bibliotecas!)
 * (são cabeçalhos de bibliotecas)
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

/*
 * Definições
 */

#define PORT 8080
#define BUFFER_SIZE 1024

/*
 * Protótipos
 */

int soma(int a, int b);

/*
 * Função principal
 * @brief Configura o servidor TCP e aguarda conexões
 * @return Código de saída
 */
int main(void) {
    int server_fd;  // descritor do socket do servidor
    int client_fd;  // descritor do socket do cliente
    struct sockaddr_in address; // estrutura de endereço
    int addrlen = sizeof(address);  // tamanho do endereço
    char buffer[BUFFER_SIZE];   // buffer para dados recebidos

    // Cria socket TCP
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    // Configura endereço
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    // Associa o socket
    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("bind");
        exit(EXIT_FAILURE);
    }

    // Escuta conexões
    if (listen(server_fd, 3) < 0) {
        perror("listen");
        exit(EXIT_FAILURE);
    }

    printf("Servidor RPC esperando na porta %d...\n", PORT);

    while (1) {
        // Aceita conexão
        if ((client_fd = accept(server_fd, (struct sockaddr *)&address, (socklen_t *)&addrlen)) < 0) {
            perror("accept");
            exit(EXIT_FAILURE);
        }

        memset(buffer, 0, BUFFER_SIZE);         // Limpa o buffer
        read(client_fd, buffer, BUFFER_SIZE);   // Lê dados do cliente
        printf("Recebido: %s\n", buffer);       // Exibe dados recebidos

        // Extrai os parâmetros "a" e "b" do JSON (simples, sem parser real)
        int a, b;
        sscanf(buffer, "{ \"function\": \"soma\", \"a\": %d, \"b\": %d }", &a, &b);

        int resultado = soma(a, b);

        // Cria resposta JSON
        char resposta[BUFFER_SIZE];
        snprintf(resposta, BUFFER_SIZE, "{ \"result\": %d }", resultado);

        write(client_fd, resposta, strlen(resposta));   // Envia resposta ao cliente
        close(client_fd);                               // Fecha conexão com o cliente
    }

    return 0;
}

/**
 * Função soma
 * @brief Implementação da função soma
 * @param a Primeiro inteiro
 * @param b Segundo inteiro
 * @return Soma de a e b
 */
int soma(int a, int b) {
    return a + b;
}
