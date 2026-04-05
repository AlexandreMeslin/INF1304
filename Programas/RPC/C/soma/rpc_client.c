/**
 * @file rpc_client.c
 * @brief Cliente RPC simples em C usando JSON sobre TCP
 * 
 * Compilar com: gcc -Wall -o rpc_client rpc_client.c -std=c11
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
 * Função principal
 * @brief Configura o cliente TCP, conecta ao servidor e realiza a chamada RPC
 * @param argc Número de argumentos
 * @param argv Argumentos (a e b, inteiros para soma)
 * @return Código de saída
 */
int main(int argc, char const *argv[]) {
    int sock = 0;                   // descritor do socket
    struct sockaddr_in serv_addr;   // estrutura de endereço do servidor
    char buffer[BUFFER_SIZE];       // buffer para dados recebidos

    // Verifica argumentos
    if (argc != 3) {
        printf("Uso: %s <a> <b>\n", argv[0]);
        return -1;
    }

    // Converte argumentos para inteiros
    int a = atoi(argv[1]);
    int b = atoi(argv[2]);

    // Cria socket
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("socket");
        return -1;
    }

    serv_addr.sin_family = AF_INET;     // IPv4
    serv_addr.sin_port = htons(PORT);   // Porta do servidor

    // Conecta ao servidor localhost
    if (inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr) <= 0) {
        perror("inet_pton");
        return -1;
    }

    // Estabelece conexão
    if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        perror("connect");
        return -1;
    }

    // Cria requisição JSON
    char request[BUFFER_SIZE];
    snprintf(request, BUFFER_SIZE, "{ \"function\": \"soma\", \"a\": %d, \"b\": %d }", a, b);

    // Envia requisição
    send(sock, request, strlen(request), 0);

    // Recebe resposta
    memset(buffer, 0, BUFFER_SIZE);
    read(sock, buffer, BUFFER_SIZE);
    printf("Resposta do servidor: %s\n", buffer);

    close(sock);    // Fecha socket

    return 0;
}
