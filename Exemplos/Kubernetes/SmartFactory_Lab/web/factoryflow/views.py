from django.http import JsonResponse
import os
import pika
from datetime import datetime
import uuid

def home(request):
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
