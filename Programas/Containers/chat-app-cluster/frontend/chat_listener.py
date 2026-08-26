'''
    Cliente de escuta de mensagens do servidor WebSocket.
    Este cliente se conecta a um servidor WebSocket e escuta as mensagens recebidas.
    O cliente utiliza a biblioteca websocket-client para gerenciar a conexão WebSocket.
    O cliente implementa callbacks para lidar com eventos de conexão, recebimento de mensagens,
    erros e fechamento da conexão.
    @author: meslin
    @version: 1.0
    @since: 2024-06-15
'''
import websocket

# Endereço do servidor WebSocket do ChatConsumer
WS_SERVER = "ws://localhost:8081/chat/ws"

# --------------------
# Função de callback para quando a conexão é aberta
# --------------------
def on_open(ws):
    '''
    Função chamada quando a conexão WebSocket é aberta.
    Permite que o cliente escute as mensagens do servidor.
    Args:
        ws (websocket.WebSocketApp): A instância do WebSocket.
    '''
    print("[INFO] Conectado ao ChatConsumer (Java).")
    print("Aguardando mensagens de broadcast...")
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
    print(f"<<< Mensagem recebida: {message}")
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
    print(f"[ERROR] Erro: {error}")
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
    print(f"[INFO] Conexão fechada: {close_msg}")
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