# Context Net Kafka Gateway
Docker-compose file to start Context Net Kafka Gateway

To start the Gateway (and all others kafka services) the developer must run:

$ docker-compose -f docker-start-gw.yml up

To stop all containers:

$ docker-compose -f docker-start-gw.yml down

To see all container running:

$ docker ps -a

All the network configurations are in this file.

Examples: 

- Processing node & GroupDefiner: https://github.com/AlexandreMeslin/Example-MUSANet-Desktop

- Mobile node: https://github.com/AlexandreMeslin/Example-MUSANet-Mobile
