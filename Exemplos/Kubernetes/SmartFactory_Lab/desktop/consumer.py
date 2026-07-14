import pika 
import json 
import time 

# ------------------------------
# Configurações de conexão com o RabbitMQ
# ------------------------------
HOST = 'localhost' 
#HOST = 'rabbitmq-service' 
USERNAME = 'admin' 
PASSWORD = 'admin123' 
  
# ------------------------------
# Função de callback para processar mensagens recebidas
# ------------------------------
def callback(ch, method, properties, body):
    '''
    Callback function to handle incoming messages.
    This function is called whenever a message is received from the queue.
    It processes the message and acknowledges it after processing.

    :param ch: Channel
    :param method: Method frame
    :param properties: Properties
    :param body: Message body
    '''
    message = json.loads(body) 
    print(f"Recebido: {message}") 
  
    # simula processamento demorado 
    time.sleep(5)
    ch.basic_ack(delivery_tag=method.delivery_tag)   
    print("Processado")
    return

 # ------------------------------
 # Função principal para iniciar o consumidor
 # ------------------------------
def main():
    # Establish connection to RabbitMQ server
    connection = pika.BlockingConnection( 
        pika.ConnectionParameters( 
            host=HOST, 
            port=5672, 
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
    
    # Set the prefetch count to 1 to ensure fair dispatching of messages
    channel.basic_qos(prefetch_count=1) 
    
    # Start consuming messages from the queue with the specified callback function
    channel.basic_consume( 
        queue='sensor-data', 
        on_message_callback=callback 
    ) 
    
    print("Aguardando mensagens...") 
    
    # Start consuming messages from the queue
    channel.start_consuming()
    return

# ------------------------------
# Entry point of the script
# ------------------------------
if __name__ == "__main__":
    main()
