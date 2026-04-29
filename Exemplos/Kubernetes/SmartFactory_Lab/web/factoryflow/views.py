import os
import pika
from django.http import HttpResponse
from datetime import datetime

def home(request):
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
        channel.basic_publish(exchange='', routing_key='fila_producao', body=msg)
        connection.close()

        return HttpResponse(f"""
        <h1>FactoryFlow Django</h1>
        <p>Job enviado!</p>
        <p>Pod: {os.uname().nodename}</p>
        <p>{msg}</p>
        """)

    except Exception as e:
        return HttpResponse(f"<h1>Erro</h1><pre>{e}</pre>")