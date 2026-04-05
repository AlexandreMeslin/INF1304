import websocket

# Endereço do servidor WebSocket do ChatConsumer
WS_SERVER = "ws://localhost:8081/chat/ws"

def on_open(ws):
    print("[INFO] Conectado ao ChatConsumer (Java).")
    print("Aguardando mensagens de broadcast...")
    return

def on_message(ws, message):
    print(f"<<< Mensagem recebida: {message}")
    return

def on_error(ws, error):
    print(f"[ERROR] Erro: {error}")
    return

def on_close(ws, close_status_code, close_msg):
    print("[INFO] Conexão fechada.")
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