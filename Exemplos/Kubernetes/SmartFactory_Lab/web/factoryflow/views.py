from django.http import JsonResponse
import os
import pika
from datetime import datetime
import uuid

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
            "timestamp": datetime.now().isoformat()
        }
        status_code = 200
    except Exception as e:
        json_response = {
            "status": "error",
            "error": str(e)
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
        "timestamp": datetime.now().isoformat()
    }
    return JsonResponse(json_response)
