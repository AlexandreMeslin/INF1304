from django.http import JsonResponse
import os
import pika
from datetime import datetime
import uuid

# --------------------------------------------------------------------------
# Simula um tempo de inicialização do container (opcional)
# Remover os comentários para ativar a simulação de tempo de inicialização
# --------------------------------------------------------------------------
#import time
#time.sleep(5)  # Simula um tempo de inicialização do container

# --------------------------------------------------------------------------
# Endpoint principal para criar um job de produção
# --------------------------------------------------------------------------
def home(request):
    '''
    Endpoint principal para criar um job de produção.
    Retorna um JSON com status "ok", mensagem de confirmação e informações do pod.
    '''
    request_id = str(uuid.uuid4())
    msg = f"Job criado em {datetime.now()}"

    try:
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host='rabbitmq-service',
                credentials=pika.PlainCredentials('admin', 'admin123')
            )
        )

        channel = connection.channel()
        channel.queue_declare(queue='fila_producao')
        channel.basic_publish(
            exchange='', 
            routing_key='fila_producao', 
            body=msg
        )
        connection.close()

        json_response = {
            "status": "ok",
            "message": msg,
            "pod": os.uname().nodename,
            "request_id": request_id,
            "timestamp": datetime.now().isoformat(),
        }
        status_code = 200
    except Exception as e:
        json_response = {
            "status": "error",
            "error": str(e),
        }
        status_code = 500

    return JsonResponse(json_response, status=status_code)


# --------------------------------------------------------------------------
# Endpoint de health check
# --------------------------------------------------------------------------
def health(request):
    '''
    Endpoint de health check para monitoramento do serviço.
    Retorna um JSON com status "ok", nome do pod e timestamp atual.
    '''
    json_response = {
        "status": "ok",
        "pod": os.uname().nodename,
        "timestamp": datetime.now().isoformat(),
        'version': '2',
    }
    return JsonResponse(json_response)

# --------------------------------------------------------------------------
# Endpoint de readiness check
# --------------------------------------------------------------------------
def readiness(request):
    '''
    Endpoint de readiness check para verificar se o container está pronto para receber tráfego.
    Retorna um JSON com status "ready" e timestamp atual.
    '''
    json_response = {
        "status": "ready",
        'pod': os.uname().nodename,
        "timestamp": datetime.now().isoformat(),
    }
    return JsonResponse(json_response)

# --------------------------------------------------------------------------
# Endpoint de liveness check
# --------------------------------------------------------------------------
def liveness(request):
    '''
    Endpoint de liveness check para verificar se o container está vivo.
    Retorna um JSON com status "alive" e timestamp atual.
    '''
    json_response = {
        "status": "alive",
        'pod': os.uname().nodename,
        "timestamp": datetime.now().isoformat(),
    }
    return JsonResponse(json_response)

# --------------------------------------------------------------------------
# Endpoint para criar consumo de CPU
# --------------------------------------------------------------------------
def cpu(request):
    '''
    Endpoint para consumir CPU do container.
    Retorna um JSON com status "ok", nome do pod, timestamp atual e uma string
    '''
    # Simula consumo de CPU
    result = 0
    for i in range(10**7):
        result += i*i
    json_response = {
        "status": "ok",
        'pod': os.uname().nodename,
        "timestamp": datetime.now().isoformat(),
        "result": result,
    }
    return JsonResponse(json_response)

# --------------------------------------------------------------------------
# Função auxiliar para enviar mensagens para a fila RabbitMQ
# --------------------------------------------------------------------------
def send_message_to_queue(message):
    '''
    Função auxiliar para enviar mensagens para a fila RabbitMQ.
    '''
    try:
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host='rabbitmq-service',
                credentials=pika.PlainCredentials('admin', 'admin123')
            )
        )
        channel = connection.channel()
        channel.queue_declare(queue='fila_producao')
        channel.basic_publish(
            exchange='', 
            routing_key='fila_producao', 
            body=message
        )
        connection.close()
    except Exception as e:
        print(f"Erro ao enviar mensagem para a fila: {e}")
