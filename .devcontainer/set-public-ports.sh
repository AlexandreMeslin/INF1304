#!/usr/bin/env bash

# Lista de portas que você quer sempre públicas
PORTS=(3000 3001 8000 8080)

for PORT in "${PORTS[@]}"; do
    echo "Tornando porta $PORT pública..."
    gh codespace ports visibility $PORT:public -c $CODESPACE_NAME
done
