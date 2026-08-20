'''
    Cliente de envio de mensagens para o servidor WebSocket.
    Este cliente se conecta a um servidor WebSocket e permite o envio de mensagens.
    O cliente utiliza a biblioteca websocket-client para gerenciar a conexão WebSocket.
    O cliente envia mensagens no formato JSON, contendo o usuário, a mensagem e o timestamp.
    O cliente também implementa callbacks para lidar com eventos de conexão, recebimento de mensagens,
    erros e fechamento da conexão.
    @author: meslin
    @version: 1.0
    @since: 2024-06-15
'''
import websocket
import json
import time

# Endereço do servidor WebSocket
WS_SERVER = "ws://localhost:8080/chat/ws"

# --------------------
# Função de callback para quando a conexão é aberta
# --------------------
def on_open(ws):
    '''
    Função chamada quando a conexão WebSocket é aberta.
    Permite que o usuário digite mensagens para enviar ao servidor.
    Args:
        ws (websocket.WebSocketApp): A instância do WebSocket.
    '''
    print("[INFO] Conexão aberta com o servidor WebSocket.")
    print("Digite suas mensagens abaixo. Para sair, digite 'exit'.")

    try:
        while True:
            user = input("Usuário: ").strip()
            if user.lower() == "exit":
                break

            message = input("Mensagem: ").strip()
            if message.lower() == "exit":
                break

            msg = {
                "user": user,
                "message": message,
                "timestamp": time.time()
            }

            ws.send(json.dumps(msg))
            print(f">>> Enviado: {msg}")

    except KeyboardInterrupt:
        print("\n[INFO] Encerrando cliente...")

    ws.close()
    return

# --------------------
# Função de callback para quando uma mensagem é recebida
# --------------------
def on_message(ws, message):
    '''
    Função chamada quando uma mensagem é recebida do servidor WebSocket.
    Args:
        ws (websocket.WebSocketApp): A instância do WebSocket.
        message (str): A mensagem recebida do servidor.
    '''
    print(f"<<< Recebido do servidor: {message}.")
    return

# --------------------
# Função de callback para quando ocorre um erro na conexão
# --------------------
def on_error(ws, error):
    '''
    Função chamada quando ocorre um erro na conexão WebSocket.
    Args:
        ws (websocket.WebSocketApp): A instância do WebSocket.
        error (Exception): O erro ocorrido.
    '''
    print(f"[ERROR] Erro: {error}.")
    return

# --------------------
# Função de callback para quando a conexão é fechada
# --------------------
def on_close(ws, close_status_code, close_msg):
    '''
    Função chamada quando a conexão WebSocket é fechada.
    Args:
        ws (websocket.WebSocketApp): A instância do WebSocket.
        close_status_code (int): Código de status do fechamento da conexão.
        close_msg (str): Mensagem de fechamento da conexão.
    '''
    print("[INFO] Conexão fechada com o servidor WebSocket.")
    return

if __name__ == "__main__":
    websocket.enableTrace(False)
    ws = websocket.WebSocketApp(
        WS_SERVER,
        on_open=on_open,
        on_message=on_message,
        on_error=on_error,
        on_close=on_close
    )
    ws.run_forever()