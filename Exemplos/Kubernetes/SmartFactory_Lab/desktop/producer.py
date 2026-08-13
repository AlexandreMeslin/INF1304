import pika
import json
import time
import random
import os
from dotenv import load_dotenv

# ------------------------------
# Configurações de conexão com o RabbitMQ
# ------------------------------
load_dotenv()  # Load environment variables from .env file
HOST = os.getenv('RABBITMQ_HOST', 'rabbitmq-service')
#HOST = 'rabbitmq-service' 
PORT = int(os.getenv('RABBITMQ_PORT', 5672))
USERNAME = os.getenv('RABBITMQ_USER', 'guest')
PASSWORD = os.getenv('RABBITMQ_PASSWORD', 'guest123')

# ------------------------------
# Função principal para iniciar o produtor
# ------------------------------
def main():
    # Establish connection to RabbitMQ server
    connection = pika.BlockingConnection(
        pika.ConnectionParameters(
            host=HOST,
            port=PORT,
            credentials=pika.PlainCredentials(
                username=USERNAME,
                password=PASSWORD
            )
        )
    )

    # Create a channel and declare the queue
    channel = connection.channel()
    channel.queue_declare(
        queue='sensor-data',    # Queue name
        durable=True            # Queue will survive broker restarts
    )

    while True:
        # Generate a random sensor data message
        message = {
            "sensor": f"S{random.randint(1,5)}",
            "temperature": round(random.uniform(20, 40), 1)
        }

        # Publish the message to the queue with delivery mode set to 2 (persistent)
        channel.basic_publish(
            exchange='',
            routing_key='sensor-data',
            body=json.dumps(message),
            properties=pika.BasicProperties(delivery_mode=2)
        )

        print(f"Enviado: {message}")

        time.sleep(1)
    return

if __name__ == "__main__":
    main()
